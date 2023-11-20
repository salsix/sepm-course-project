import { Component, OnInit } from '@angular/core';
import {BasicError} from '../../classes/basicError';
import {ActivatedRoute, Router} from '@angular/router';
import {TicketsService} from '../../services/tickets.service';
import {Bill} from '../../dtos/bill';

@Component({
  selector: 'app-user-tickets',
  templateUrl: './user-tickets.component.html',
  styleUrls: ['./user-tickets.component.scss']
})
export class UserTicketsComponent extends BasicError implements OnInit {
  bought: Bill[] = [];
  reserved: Bill[] = [];
  past: Bill[] = [];

  maxPage = 100000000;
  pageOld = 0;
  page = 0;
  pageLoad = false;

  constructor(private route: ActivatedRoute, private router: Router, private ticketsService: TicketsService) {
    super(router);
  }

  sort(a: Bill, b: Bill) {
    let delta = new Date(a.show.date).valueOf() - new Date(b.show.date).valueOf();
    if(delta!==0){
      return delta;
    }

    delta = a.show.hour - b.show.hour;
    if(delta!==0){
      return delta;
    }

    delta = a.show.minute - b.show.minute;
    if(delta!==0){
      return delta;
    }

    delta = new Date(b.buyTime).valueOf() - new Date(a.buyTime).valueOf();
    if(delta!==0){
      return delta;
    }

    if(a.stornoTime && b.stornoTime){
      return new Date(b.stornoTime).valueOf()-new Date(a.stornoTime).valueOf();
    }
    return 0;
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe( paramMap => {
      this.page = +paramMap.page;
      if(!paramMap.page){
        this.page = 0;
      }
      this.loadOld();
    });

    this.ticketsService.getUserTickets().subscribe(
      (bills: Bill[]) => {
        this.bought = [];
        this.reserved = [];
        for(const b of bills){
          if(b.reserved){
            this.reserved.push(b);
          } else {
            this.bought.push(b);
          }
        }

        this.bought = this.bought.sort(this.sort);
        this.reserved = this.reserved.sort(this.sort);
      },
        error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );

  }

  public pagination(page: number) {
    this.pageLoad = true;
    this.pageOld = this.page;

    this.page = page-1;

    if(this.page===this.pageOld){
      this.pageLoad = false;
      return;
    }

    this.loadOld();
  }

  public loadOld(){
    this.ticketsService.getOldUserTickets(this.page).subscribe(
      (bills: Bill[]) => {
        if(bills.length===0 && this.past.length>0){
          this.basicAlert = {type: 'warning', strongMessage: '', message: 'No more old tickets', duration: 2};
          this.page = this.pageOld;
          this.pageLoad = false;
          return;
        }
        this.past = bills;
        this.pageLoad = false;
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
        this.pageLoad = false;
      }
    );
  }

  public dateString(date: string): string {
    const d = new Date(date);
    return this.ticketsService.dateString(d);
  }

  public dateStringFromString(date: string): string {
    return this.ticketsService.dateString(new Date(date));
  }

  public timeString(date: string) {
    const d = new Date(date);
    return this.timeStringNumbers(d.getHours(), d.getMinutes());
  }

  public timeStringNumbers(hour: number, minute: number): string {
    return this.ticketsService.timeString(hour, minute);
  }

  orderPdf(billId: number){
    this.ticketsService.getOrderPdf(billId).subscribe(x => {
      const blob = new Blob([x], {type: 'application/pdf'});
      if(window.navigator && window.navigator.msSaveOrOpenBlob){
        window.navigator.msSaveOrOpenBlob(blob);
        return;
      }
      const data = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = data;
      link.download = 'order.pdf';
      link.dispatchEvent(new MouseEvent('click',{bubbles: true, cancelable: true, view: window}));
      // eslint-disable-next-line prefer-arrow/prefer-arrow-functions
      setTimeout(function() {
        window.URL.revokeObjectURL(data);
        link.remove();
      },100);
    });
  }
  billPdf(billId: number){
    this.ticketsService.getBillPdf(billId).subscribe(x => {
      const blob = new Blob([x], {type: 'application/pdf'});
      if(window.navigator && window.navigator.msSaveOrOpenBlob){
        window.navigator.msSaveOrOpenBlob(blob);
        return;
      }
      const data = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = data;
      link.download = 'bill.pdf';
      link.dispatchEvent(new MouseEvent('click',{bubbles: true, cancelable: true, view: window}));
      // eslint-disable-next-line prefer-arrow/prefer-arrow-functions
      setTimeout(function() {
        window.URL.revokeObjectURL(data);
        link.remove();
      },100);
    });
  }
  stornoBillPdf(billId: number){
    this.ticketsService.getStornoBillPdf(billId).subscribe(x => {
      const blob = new Blob([x], {type: 'application/pdf'});
      if(window.navigator && window.navigator.msSaveOrOpenBlob){
        window.navigator.msSaveOrOpenBlob(blob);
        return;
      }
      const data = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = data;
      link.download = 'stornoBill.pdf';
      link.dispatchEvent(new MouseEvent('click',{bubbles: true, cancelable: true, view: window}));
      // eslint-disable-next-line prefer-arrow/prefer-arrow-functions
      setTimeout(function() {
        window.URL.revokeObjectURL(data);
        link.remove();
      },100);
    });
  }
}
