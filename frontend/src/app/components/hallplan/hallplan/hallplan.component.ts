import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Hallplan} from '../../../dtos/hallplan';
import {HallplanArea} from '../../../dtos/hallplan-area';
import {HallplanData} from '../../../dtos/hallplan-data';
import {HallplanSeat} from '../../../dtos/hallplan-seat';
import {Booked} from '../../../dtos/booked';
import {HallplanService} from '../../../services/hallplan.service';

@Component({
  selector: 'app-hallplan',
  templateUrl: './hallplan.component.html',
  styleUrls: ['./hallplan.component.scss']
})

export class HallplanComponent implements OnInit {
  @Input() canEdit = false;
  @Input() plan: Hallplan;
  @Input() booked: Booked;
  @Input() want: Booked;

  @Input() static = false;

  @Output() editing = new EventEmitter<boolean>();
  @Output() editSeat = new EventEmitter<boolean>();
  @Output() data = new EventEmitter<HallplanData>();
  @Output() editIndex = new EventEmitter<number>();
  @Output() seat = new EventEmitter<HallplanSeat>();
  @Output() area = new EventEmitter<HallplanArea>();
  @Output() seatNumber = new EventEmitter<number>();

  polygons: string[] = [];
  padding = 60;
  width = 0;
  height = 0;

  constructor() {
  }

  ngOnInit(): void {
    for(const a of this.plan.areas){
      HallplanService.updateArea(a);
    }

    if(this.canEdit){
      this.width = 200;
      this.height = 200;
    }

    //init size
    for (const s of this.plan.seats) {
      const dist = Math.sqrt(s.offsetX*s.offsetX + s.offsetY*s.offsetY) * s.count;
      const angle = Math.atan2(s.offsetY, s.offsetX);

      const w = s.positionX + Math.cos(angle) * dist;
      if (w + this.padding > this.width) {
        this.width = w + this.padding;
      }
      if (s.positionX + this.padding > this.width) {
        this.width = s.positionX + this.padding;
      }
      const h = s.positionY + Math.sin(angle) * dist;
      if (h + this.padding > this.height) {
        this.height = h + this.padding;
      }
      if (s.positionY + this.padding > this.height) {
        this.height = s.positionY + this.padding;
      }
    }
    for(const a of this.plan.areas){
      for(const p of a.positions){
        if(p.x+this.padding > this.width){
          this.width = p.x + this.padding;
        }
        if(p.y+this.padding > this.height){
          this.height = p.y + this.padding;
        }
      }
    }
  }

  public focusSeat(event: Event, data: HallplanSeat, index: number) {
    if(this.static){
      return;
    }

    this.editSeat.emit(true);
    this.editIndex.emit(index);
    this.data.emit(data);
    this.seat.emit(data);
    this.editing.emit(true);
    event.stopPropagation();
  }

  public focusArea(event: Event, polygon: HallplanArea, index: number) {
    if(this.static){
      return;
    }

    this.editSeat.emit(false);
    this.editIndex.emit(index);
    this.data.emit(polygon);
    this.area.emit(polygon);
    this.editing.emit(true);
    event.stopPropagation();
  }

  public unfocus() {
    if (!this.canEdit || this.static) {
      return;
    }
    this.editing.emit(false);
  }

  public updateWidth(w: number){
    if(w+this.padding>this.width){
      this.width = w+this.padding;
    }
  }

  public updateHeight(h: number){
    if(h+this.padding>this.height){
      this.height = h+this.padding;
    }
  }

  public trackIndex(index, item) {
    return index;
  }

}
