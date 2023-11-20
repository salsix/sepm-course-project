import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostListener,
  Input,
  Output
} from '@angular/core';
import {Hallplan} from '../../../dtos/hallplan';
import {Drag} from '../../../classes/drag';
import {HallplanArea} from '../../../dtos/hallplan-area';
import {HallplanService} from '../../../services/hallplan.service';

@Component({
  selector: 'app-hallplan-polygon',
  templateUrl: './hallplan-polygon.component.html',
  styleUrls: ['./hallplan-polygon.component.scss', '../../../app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HallplanPolygonComponent extends Drag {
  @Input() index;
  @Input() canDrag = false;

  @Input() plan: Hallplan;
  @Input() area: HallplanArea;

  @Output() width = new EventEmitter<number>();
  @Output() height = new EventEmitter<number>();

  constructor() {
    super();
  }

  @HostListener('document:mousemove', ['$event'])
  public mousemove(event: MouseEvent) {
    if (!this.canDrag || !this.dragging) {
      return;
    }
    const x = this.startX + event.clientX - this.posX;
    const y = this.startY + event.clientY - this.posY;

    const xPos = x - x % this.grid;
    const yPos = y - y % this.grid;
    const changed = this.area.positions[this.index].x !== xPos
      || this.area.positions[this.index].y !== yPos;
    this.area.positions[this.index].x = xPos;
    this.area.positions[this.index].y = yPos;

    this.width.emit(xPos);
    this.height.emit(yPos);

    if (changed) {
      HallplanService.updateArea(this.area);
    }
  }

  @HostListener('document:mouseup', ['$event'])
  public mouseUpAnywhere(event) {
    if (!this.canDrag || !this.dragging) {
      return;
    }
    this.dragging = false;
  }

  public mousedown(event: MouseEvent) {
    this.startX = this.area.positions[this.index].x;
    this.startY = this.area.positions[this.index].y;
    super.mousedown(event);
  }
}
