import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Hallplan} from '../../../dtos/hallplan';
import {HallplanSeat} from '../../../dtos/hallplan-seat';
import {HallplanCat} from '../../../dtos/hallplan-cat';
import {HallplanArea} from '../../../dtos/hallplan-area';
import {HallplanData} from '../../../dtos/hallplan-data';
import {BasicError} from '../../../classes/basicError';
import {HallplanService} from '../../../services/hallplan.service';
import {ActivatedRoute} from '@angular/router';
import {HallplanPosition} from '../../../dtos/hallplan-position';
import {Alert} from '../../../classes/alert';

@Component({
  selector: 'app-hallplan-edit',
  templateUrl: './hallplan-edit.component.html',
  styleUrls: ['./hallplan-edit.component.scss', '../../../app.component.scss']
})
export class HallplanEditComponent extends BasicError implements OnInit {
  alert: Alert;

  locId: number;
  planId: number;
  plan: Hallplan = null;
  hallplanCopy: Hallplan;

  lastIdentifier = 'R';
  lastRow = 1;
  lastArea = 1;

  priceGroup: number;

  //if selected (focused)
  editing = false;
  //if editing seat
  editSeat = true;
  //hallplan data to edit (same object as seat/area)
  data: HallplanData = {cat: 0, count: 1, identifier: 'R', rowNumber: 0};
  //seat to edit
  seat: HallplanSeat = {image: '',
    positionX: 20, positionY: 20,
    offsetY: 0, offsetX: 0,
    cat: 0,
    count: 1, startNumber: 1,
    identifier: this.lastIdentifier, rowNumber: 0
  };
  sAngle = 0;
  sDistance = 0;
  //area to edit
  area: HallplanArea = {positions: [], count: 0,
    cat: 0, identifier: 'R', rowNumber: 0};
  //index in list of plan.seats or plan.areas
  editIndex: number;
  newAreaCount = 4;

  copyFrom: HallplanSeat;
  copyTo: HallplanSeat;

  constructor(private route: ActivatedRoute, private hallplanService: HallplanService) {
    super();
  }

  private static deletedPriceIndex(index: number, list: HallplanData[]){
    for(let length=list.length, i=0; i<length; ++i){
      const s = list[i];
      if(s.cat===index){
        s.cat = 0;
      } else if(s.cat>index){
        --s.cat;
      }
    }
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe( paramMap => {
      this.locId = +paramMap.get('plan');
      this.planId = +paramMap.get('id');
      this.hallplanCopy = {id: this.planId, name: null, cats: null, seats: null, areas: null, location: null};
      this.load();
    });
  }

  public load(){
    this.hallplanService.get(this.planId).subscribe(
      (hallplan: Hallplan) => {
        if(hallplan.cats.length===0){
          hallplan.cats.push(this.defaultCat());
        }
        this.plan = hallplan;
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public save(){
    this.hallplanService.edit(this.plan).subscribe(
      () => {
        this.alert = {type: 'success', strongMessage: 'Success: ', message: 'saved hallplan'};
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: 8};
      }
    );
  }

  public defaultCat(): HallplanCat {
    return {id: null, price: 10, color: '#ff0000'};
  }

  //click on template seat and add new seat
  public newSeat(seatImage: string, distance: number){
    //add new seat
    this.plan.seats.push({
        image: seatImage,
        positionX: 40,
        positionY: 40,
        offsetY: 0,
        offsetX: distance,
        cat: 0,
        count: 1,
        startNumber: 1,
        identifier: this.lastIdentifier,
        rowNumber: this.lastRow,
      });

    ++this.lastRow;
  }

  public newPolygon(){
    const pos: HallplanPosition[] = [];
    for(let i=0; i<this.newAreaCount; ++i){
      const angle = 2*Math.PI*(i/this.newAreaCount + 1/(2*this.newAreaCount));
      pos.push({x: 100+Math.cos(angle)*50, y: 100+Math.sin(angle)*50});
    }

    const area: HallplanArea = {positions: pos, count: 100,
      cat: 0, identifier: 'S', rowNumber: this.lastArea++};
    HallplanService.updateArea(area);
    this.plan.areas.push(area);
  }

  //copy selected seat
  public copyCurrent(){
    if(this.editSeat){
      let offX = 15;
      let offY = 15;

      //same offset as last time copying
      if(this.copyFrom && this.copyTo && this.copyTo===this.seat){
        const x = this.copyTo.positionX-this.copyFrom.positionX;
        const y = this.copyTo.positionY-this.copyFrom.positionY;

        //max distance 80px
        if(x*x+y*y < 80*80){
          offX = x;
          offY = y;
        }
      }

      const newItem = { ...this.seat };
      newItem.positionX += offX;
      newItem.positionY += offY;
      ++newItem.rowNumber;
      this.plan.seats.push(newItem);

      //copy same offset preparation
      this.copyFrom = this.seat;
      this.copyTo = newItem;

      //select new seat
      this.seat.select = false;
      this.seat = newItem;
      this.data = newItem;
      this.seat.select = true;
    } else {
      const newItem = { ...this.area };
      ++newItem.rowNumber;

      const length = this.area.positions.length;
      newItem.positions = Array(length);
      for(let i=0; i<length; ++i){
        const pos: HallplanPosition = {x: 0, y: 0};
        pos.x = this.area.positions[i].x+20;
        pos.y = this.area.positions[i].y+20;
        newItem.positions[i] = pos;
      }
      HallplanService.updateArea(newItem);
      this.plan.areas.push(newItem);
      this.editing = false;
    }
    ++this.lastRow;
  }

  //delete selected seat
  public deleteCurrent(){
    if(this.editSeat){
      this.plan.seats.splice(this.editIndex, 1);
    } else{
      this.plan.areas.splice(this.editIndex, 1);
    }
    this.editing = false;
  }

  public deletePriceGroup(index: number){
    this.plan.cats.splice(index, 1);
    HallplanEditComponent.deletedPriceIndex(index, this.plan.seats);
    HallplanEditComponent.deletedPriceIndex(index, this.plan.areas);
  }

  public editingChange(){
    if(!this.editing){
      this.data.select=false;
    }
    if(!this.editSeat){
      return;
    }
    const x = this.seat.offsetX;
    const y = this.seat.offsetY;
    this.sAngle = -Math.round(Math.atan2(y, x)*180/Math.PI);
    this.sDistance = Math.round(Math.sqrt(x*x+y*y));
  }
  public changeOffset(){
    const angle = this.sAngle * Math.PI / 180;
    this.seat.offsetX = Math.cos(-angle) * this.sDistance;
    this.seat.offsetY = Math.sin(-angle) * this.sDistance;
  }

}
