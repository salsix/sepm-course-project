import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {BasicError} from '../../../classes/basicError';
import {Alert} from '../../../classes/alert';
import {HallplanData} from '../../../dtos/hallplan-data';
import {HallplanSeat} from '../../../dtos/hallplan-seat';
import {HallplanArea} from '../../../dtos/hallplan-area';
import {Booked} from '../../../dtos/booked';
import {Show} from '../../../dtos/show';
import {TicketsService} from '../../../services/tickets.service';

interface SortHelper {
  id: string;
  cat: number;
}

@Component({
  selector: 'app-hallplan-ticket',
  templateUrl: './hallplan-ticket.component.html',
  styleUrls: ['./hallplan-ticket.component.scss']
})
export class HallplanTicketComponent extends BasicError implements OnInit {
  @Input() show: Show;
  @Input() taken: Booked;
  @Input() want: Booked;
  @Input() static = false;

  @Output() alert: EventEmitter<Alert> = new EventEmitter<Alert>();
  @Output() price: EventEmitter<number> = new EventEmitter<number>();

  editSeat = true;
  data: HallplanData = null;
  seat: HallplanSeat = null;
  area: HallplanArea = null;
  seatNumber = -1;

  wantSeatIds: SortHelper[] = [];
  wantAreasIds: HallplanArea[] = [];

  total = 0;
  sCount = 0;
  aCount = 0;

  constructor() {
    super();
  }

  private static sortData(a: HallplanData, b: HallplanData): number {
    return (a.identifier+a.rowNumber).localeCompare(b.identifier+b.rowNumber);
  }

  ngOnInit() {
    //init wantSeatIds, wantAreasIds
    for(const id in this.want.seats){
      if(!this.want.seats.hasOwnProperty(id)){
        continue;
      }

      //sort
      this.want.seats[id].sort();

      //add id to list
      for(const seat of this.show.hallplan.seats){
        if(seat.identifier+seat.rowNumber===id){
          this.addSeatWant(id, seat.cat);
        }
      }

      //add count
      this.sCount += this.want.seats[id].length;
    }
    for(const id in this.want.areas){
      if(!this.want.areas.hasOwnProperty(id)){
        continue;
      }

      //add to list
      for(const area of this.show.hallplan.areas){
        if(area.identifier+area.rowNumber===id){
          this.wantAreasIds.push(area);
        }
      }

      //add count
      this.aCount += this.want.areas[id];
    }
  }

  public select(select: boolean) {
    if (!select) {
      return;
    }
    const id = this.data.identifier+this.data.rowNumber;
    if(this.editSeat){
      //is already booked
      if(this.taken.seats[id] && this.taken.seats[id].indexOf(this.seatNumber)!==-1){
        this.alert.emit({type: 'warning', strongMessage: '', message: 'Seat already booked', duration: 1});
        return;
      }

      //wanted, not wanted anymore
      if(this.want.seats[id] && this.want.seats[id].indexOf(this.seatNumber)!==-1){
        const ar = this.want.seats[id];
        const index = ar.indexOf(this.seatNumber);
        ar.splice(index, 1);

        //update list
        this.wantSeatIds = this.wantSeatIds.filter((s: SortHelper) => this.want.seats[s.id].length>0);

        //update total
        this.calcTotal();
        return;
      }

      //want
      if(!this.want.seats[id]){
        this.want.seats[id] = [this.seatNumber];
      } else {
        const list = this.want.seats[id];
        list.push(this.seatNumber);
        list.sort((a: number, b: number) => a<b ? -1 : 1);
      }

      //add id to list of ids
      this.addSeatWant(id, this.seat.cat);

      //calc total
      this.calcTotal();
    } else {
      //area

      //no space left
      const bookedCount = this.areaBooked(id);
      if(bookedCount===this.area.count){
        this.alert.emit({type: 'warning', strongMessage: '', message: 'No space left', duration: 1});
        return;
      } else if(bookedCount>this.area.count){
        this.alert.emit({type: 'danger', strongMessage: 'Error: ', message: 'Too many bookings', duration: 1});
        return;
      }

      //want
      if(!this.want.areas[id]){
        this.want.areas[id] =  1;
      } else {
        const count = this.want.areas[id];
        this.want.areas[id] =  count+1;
      }

      //add id to list of ids
      if(this.wantAreasIds.indexOf(this.area)===-1){
        this.wantAreasIds.push(this.area);
        this.wantAreasIds.sort(HallplanTicketComponent.sortData);
      }

      //calc total
      this.calcTotal();
    }
  }

  public changeArea(event: Event, area: HallplanArea){
    let value: number = +event.target['value'];
    const id = area.identifier+area.rowNumber;

    //set new value
    this.want.areas[id] =  value;

    //check max
    if(value>0){
      const bookedCount = this.areaBooked(id)+value;
      if(bookedCount>area.count){
        this.alert.emit({type: 'warning', strongMessage: '', message: 'Not enough space', duration: 1});
        value -= bookedCount-area.count;
        this.want.areas[id] =  value;
        event.target['value'] = value;
      }
    }

    //update list
    if(value===0){
      delete this.want.areas[area.identifier+area.rowNumber];
      this.wantAreasIds = this.wantAreasIds.filter((a: HallplanArea) => this.want.areas[a.identifier+a.rowNumber]>0);
    }

    //calc total
    this.calcTotal();
  }

  public areaBooked(id: string): number {
    let bookedCount = 0;
    if(this.taken.areas[id]){
      bookedCount += this.taken.areas[id];
    }
    return bookedCount;
  }

  public hasBorder(id: string, nr: number, index: number){
    const list = this.want.seats[id];
    if(index+1===list.length){
      return true;
    }
    if(list[index+1]===nr+1){
      return null;
    }
    return true;
  }

  public listRemove(id: string, nr: number) {
    if(this.static){
      return;
    }

    const list = this.want.seats[id];
    this.want.seats[id] = list.filter((n: number) => n!==nr);
    this.calcTotal();
  }

  public calcTotal(){
    this.total = 0;
    this.sCount = 0;
    this.aCount = 0;

    for(const s of this.wantSeatIds){
      const count = this.want.seats[s.id].length;
      this.total += count*this.show.hallplan.cats[s.cat].price;
      this.sCount += count;
    }
    for(const a of this.wantAreasIds){
      const count = this.want.areas[a.identifier+a.rowNumber];
      this.total += count*this.show.hallplan.cats[a.cat].price;
      this.aCount += count;
    }

    this.price.emit(this.total);
  }

  private addSeatWant(identifier: string, cat: number){
    const helper: SortHelper = {id: identifier, cat};
    if(this.wantSeatIds.filter((s: SortHelper) => s.id===identifier && s.cat===cat).length===0){
      this.wantSeatIds.push(helper);
      this.wantSeatIds.sort((a: SortHelper, b: SortHelper) => a.id.localeCompare(b.id));
    }
  }

}
