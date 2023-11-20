import { Component, OnInit } from '@angular/core';
import {BasicError} from '../../classes/basicError';
import {Bill} from '../../dtos/bill';
import {ActivatedRoute, Router} from '@angular/router';
import {TicketsService} from '../../services/tickets.service';
import {Booked} from '../../dtos/booked';

@Component({
  selector: 'app-user-bill',
  templateUrl: './user-bill.component.html',
  styleUrls: ['./user-bill.component.scss']
})
export class UserBillComponent extends BasicError implements OnInit {
  id: number;
  bill: Bill;

  taken: Booked;
  want: Booked;
  price = 0;

  constructor(private route: ActivatedRoute, private router: Router, public ticketsService: TicketsService) {
    super(router);
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe( paramMap => {
      this.id = +paramMap.get('id');
      this.load();
    });
  }

  public load(){
    this.want = null;
    this.ticketsService.getBill(this.id).subscribe(
      (bill: Bill) => {
        console.log(bill);
        this.bill = bill;

        this.taken = bill.booked;

        this.want = bill.want;
        this.want.show = bill.show;

        this.price = this.bill.price;
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public buyReserved(){
    if(!this.bill.reserved){
      return;
    }

    this.ticketsService.buyReservedTickets(this.bill.show.id, this.id, this.want).subscribe(
      (newId: number) => {
        this.basicAlert = {type: 'success', strongMessage: 'Success: ',
          message: 'Bought tickets!', duration: -1};
        this.router.navigate(['user/tickets', newId], {}).then(() => {});
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public storno(){
    this.ticketsService.stornoTickets(this.id).subscribe(
      () => {
        const a = {type: 'success', strongMessage: 'Success: ',
          message: 'Cancelled tickets!', duration: -1};
        this.router.navigate(['user/tickets'], {state: {alert: a}}).then(() => {});
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

}
