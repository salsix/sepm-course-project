import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TicketsService} from '../../services/tickets.service';
import {BasicError} from '../../classes/basicError';
import {Show} from '../../dtos/show';
import {Booked} from '../../dtos/booked';
import {Even} from '../../dtos/even';
import {ShowService} from '../../services/show.service';

@Component({
  selector: 'app-show-tickets',
  templateUrl: './show-tickets.component.html',
  styleUrls: ['./show-tickets.component.scss']
})
export class ShowTicketsComponent extends BasicError implements OnInit {
  showId: number;
  show: Show;
  taken: Booked;

  want: Booked;
  price = 0;
  submitting = false;

  constructor(private route: ActivatedRoute, private router: Router,
              public ticketsService: TicketsService, private showService: ShowService) {
    super();
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(paramMap => {
      this.showId = +paramMap.get('id');
      this.load();
    });
  }

  public load() {
    this.showService.getBooked(this.showId).subscribe(
      (b: Booked) => {
        console.log('success', b);

        //set fields
        this.taken = b;
        this.show = b.show;
        this.want = {seats: {}, areas: {}, show: null};
        this.want.show = {id: b.show.id, hallplan: null, date: null, event: null, hour: null, minute: null};
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public successMessage(price: number): string {
    const show: Show = this.taken.show;
    const event: Even = show.event;
    return 'tickets ('+price+'$) for<br><h4>'+event.name+'</h4> on '+show.date+' '+show.hour+':'+show.minute;
  }

  public buy(){
    this.submitting = true;
    this.ticketsService.buyTickets(this.showId, this.want).subscribe(
      (price: number) => {
        console.log('success', price);
        this.submitting = false;

        const a = {type: 'success', strongMessage: 'Success: ',
          message: 'Bought '+this.successMessage(price), duration: -1};
        this.router.navigate(['user/tickets'], {state: {alert: a}}).then(() => {});
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
        this.submitting = false;
      }
    );
  }
  public reserve(){
    this.submitting = true;
    this.ticketsService.reserveTickets(this.showId, this.want).subscribe(
      (price: number) => {
        console.log('success', price);
        this.submitting = false;

        const a = {type: 'success', strongMessage: 'Success: ',
          message: 'Reserved '+this.successMessage(price)+'.<br>Don\'t forget to get your tickets before the show!' , duration: -1};
        this.router.navigate(['user/tickets'], {state: {alert: a}}).then(() => {});
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
        this.submitting = false;
      }
    );
  }
  public showInFuture(show: Show): boolean {
    return ShowService.showInFuture(show);
  }
}
