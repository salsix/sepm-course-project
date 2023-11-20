import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostListener,
  Input,
  OnInit,
  Output
} from '@angular/core';
import {HallplanArea} from '../../../dtos/hallplan-area';
import {Drag} from '../../../classes/drag';
import {Booked} from '../../../dtos/booked';
import {HallplanService} from '../../../services/hallplan.service';
import {Hallplan} from '../../../dtos/hallplan';

@Component({
  selector: 'app-hallplan-polygon-text',
  templateUrl: './hallplan-polygon-text.component.html',
  styleUrls: ['./hallplan-polygon-text.component.scss', '../../../app.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HallplanPolygonTextComponent extends Drag implements OnInit {
  @Input() canDrag = false;

  @Input() plan: Hallplan;
  @Input() area: HallplanArea;
  @Input() booked: Booked;
  @Input() want: Booked;

  @Output() width = new EventEmitter<number>();
  @Output() height = new EventEmitter<number>();

  constructor() {
    super();
  }

  @HostListener('document:mousemove', ['$event'])
  public mousemove(event: MouseEvent){
    if(!this.canDrag || !this.dragging){
      return;
    }
    const x = event.clientX-this.posX;
    const y = event.clientY-this.posY;

    const dx = x-x%this.grid;
    const dy = y-y%this.grid;

    if(dx!==0 || dy!==0){
      const length = this.area.positions.length;
      let w = 0;
      let h = 0;
      for(let i=0; i<length; ++i){
        this.area.positions[i].x += dx;
        this.area.positions[i].y += dy;

        if(this.area.positions[i].x>w){
          w = this.area.positions[i].x;
        }
        if(this.area.positions[i].y>h){
          h = this.area.positions[i].y;
        }
      }

      this.posX += dx;
      this.posY += dy;

      this.width.emit(w);
      this.height.emit(h);

      HallplanService.updateArea(this.area);
    }
  }

  @HostListener('document:mouseup', ['$event'])
  public mouseUpAnywhere(event){
    if(!this.canDrag || !this.dragging){
      return;
    }
    this.dragging = false;
  }

  public mousedown(event: MouseEvent) {
    super.mousedown(event);
  }

  ngOnInit(): void {
  }
}
