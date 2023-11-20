package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillSimpleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookedDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BillMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BookedMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import com.itextpdf.html2pdf.HtmlConverter;
import io.swagger.v3.oas.annotations.Operation;
import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@RestController
@RequestMapping(value = TicketEndpoint.BASE_URL)
public class TicketEndpoint {

    private static final Logger LOGGER = LoggerFactory
        .getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_URL = "api/v1/tickets";
    private final TemplateEngine templateEngine;

    private final BookedMapper bookedMapper;
    private final BillMapper billMapper;
    private final TicketService ticketService;
    @Autowired
    ServletContext servletContext;

    public TicketEndpoint(BookedMapper bookedMapper, BillMapper billMapper,
        TicketService ticketService, TemplateEngine templateEngine) {
        this.bookedMapper = bookedMapper;
        this.billMapper = billMapper;
        this.ticketService = ticketService;
        this.templateEngine = templateEngine;
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "")
    @Operation(summary = "gets all future bills from logged in user")
    List<BillSimpleDto> getBills() {
        LOGGER.info("GET " + BASE_URL + "");
        return billMapper.entityToSimpleDto(ticketService.getUserBills());
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/past")
    @Operation(summary = "gets all past bills from logged in user")
    List<BillSimpleDto> getBillsPast(@RequestParam("page") int page) {
        LOGGER.info("GET " + BASE_URL + "/past?page={}", page);
        return billMapper.entityToSimpleDto(ticketService.getPastUserBills(page));
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/{billId}")
    @Operation(summary = "gets bill by id")
    BillDto getBill(@PathVariable("billId") Long billId) {
        LOGGER.info("GET " + BASE_URL + "/{}", billId);
        return billMapper.entityToDto(ticketService.getById(billId));
    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/buy")
    @Operation(summary = "buy tickets for show")
    double buyTickets(@RequestBody BookedDto bookedDto) {
        LOGGER.info("POST " + BASE_URL + "/buy {}", bookedDto);
        return ticketService.bookNew(bookedMapper.dtoToEntity(bookedDto), false).getPrice();
    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/reserve")
    @Operation(summary = "reserve tickets for show")
    double reserveTickets(@RequestBody BookedDto bookedDto) {
        LOGGER.info("POST " + BASE_URL + "/reserve {}", bookedDto);
        return ticketService.bookNew(bookedMapper.dtoToEntity(bookedDto), true).getPrice();
    }

    @Secured("ROLE_USER")
    @PostMapping(value = "/{billId}/buy")
    @Operation(summary = "buy tickets for show that were reserved before")
    Long buyReservedTickets(@PathVariable("billId") Long billId, @RequestBody BookedDto bookedDto) {
        LOGGER.info("PUT " + BASE_URL + "/{}/buy {}", billId, bookedDto);
        return ticketService.buyReserved(billId, bookedMapper.dtoToEntity(bookedDto));
    }

    @Secured("ROLE_USER")
    @DeleteMapping(value = "/{billId}")
    @Operation(summary = "storno tickets")
    double stornoTickets(@PathVariable("billId") Long billId) {
        LOGGER.info("DELETE " + BASE_URL + "/{}", billId);
        return ticketService.storno(billId, false);
    }

    @PermitAll
    @GetMapping(value = "/{billId}/billPdf")
    @Operation(summary = "billPdf")
    public ResponseEntity<?> getBillPdf(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("billId") Long billId) {

        String billHtml;
        Bill bill = ticketService.getById(billId);
        if (!bill.getStorno()) {
            WebContext context = new WebContext(request, response, servletContext);
            context.setVariable("billEntry", bill);
            billHtml = templateEngine.process("bill.html", context);

            ByteArrayOutputStream target = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(billHtml, target);

            byte[] bytes = target.toByteArray();
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
        } else {
            return null;
        }
    }

    @PermitAll
    @GetMapping(value = "/{billId}/stornoBillPdf")
    @Operation(summary = "stornoBillPdf")
    public ResponseEntity<?> getStornoBillPdf(HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("billId") Long billId) {

        String stornoBillHtml;
        Bill bill = ticketService.getById(billId);
        if (bill.getStorno()) {
            WebContext context = new WebContext(request, response, servletContext);
            context.setVariable("stornoBillEntry", bill);
            stornoBillHtml = templateEngine.process("storno-bill.html", context);

            ByteArrayOutputStream target = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(stornoBillHtml, target);

            byte[] bytes = target.toByteArray();
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
        } else {
            return null;
        }
    }

    @PermitAll
    @GetMapping(value = "/{billId}/orderPdf")
    @Operation(summary = "orderPdf")
    public ResponseEntity<?> getOrderPdf(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("billId") Long billId) {

        String orderHtml;
        Bill orderBill = ticketService.getById(billId);
        String qrImage = ticketService.orderQr(billId);

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("orderEntry", orderBill);
        context.setVariable("qr", qrImage);
        orderHtml = templateEngine.process("order.html", context);

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(orderHtml, target);

        byte[] bytes = target.toByteArray();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(bytes);

    }
}
