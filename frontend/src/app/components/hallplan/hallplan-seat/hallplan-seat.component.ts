import {
  ChangeDetectionStrategy, ChangeDetectorRef,
  Component,
  EventEmitter,
  HostListener,
  Input,
  Output
} from '@angular/core';
import {Drag} from '../../../classes/drag';
import {HallplanSeat} from '../../../dtos/hallplan-seat';
import {Hallplan} from '../../../dtos/hallplan';
import {Booked} from '../../../dtos/booked';

@Component({
  selector: 'app-hallplan-seat',
  templateUrl: './hallplan-seat.component.html',
  styleUrls: ['./hallplan-seat.component.scss', '../../../app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HallplanSeatComponent extends Drag {
  @Input() index;
  @Input() canDrag = false;
  @Input() booked: Booked;
  @Input() want: Booked;

  @Input() seat: HallplanSeat;
  @Input() plan: Hallplan;

  @Output() seatNumber = new EventEmitter<number>();

  @Output() width = new EventEmitter<number>();
  @Output() height = new EventEmitter<number>();

  constructor(private ref: ChangeDetectorRef) {
    super();
  }

  @HostListener('document:mousemove', ['$event'])
  public mousemove(event: MouseEvent) {
    if (!this.canDrag || !this.dragging) {
      return;
    }
    const x = this.startX + event.clientX - this.posX;
    const y = this.startY + event.clientY - this.posY;
    this.seat.positionX = x - x % this.grid;
    this.seat.positionY = y - y % this.grid;

    const dist = this.seat.offsetX * this.seat.count;
    const angle = this.seat.offsetY * Math.PI / 180;

    let w = this.seat.positionX;
    let h = this.seat.positionY;
    const px = this.seat.positionX + dist * Math.cos(angle);
    if(px>w){
      w = px;
    }
    const py = this.seat.positionY + dist * Math.sin(angle);
    if(py>h){
      h = py;
    }
    this.width.emit(w);
    this.height.emit(h);
  }

  @HostListener('document:mouseup', ['$event'])
  public mouseUpAnywhere(event) {
    if (!this.canDrag || !this.dragging) {
      return;
    }
    this.dragging = false;
  }

  public mousedownCustom(event: MouseEvent, index: number) {
    this.seatNumber.emit(this.seat.startNumber+index);
    this.startX = this.seat.positionX;
    this.startY = this.seat.positionY;
    super.mousedown(event);
    this.ref.markForCheck();
  }

  public ar(count: number): number[] {
    return Array(count);
  }
}
