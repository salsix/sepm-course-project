import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Alert} from '../../classes/alert';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss'],
  animations: [
    trigger('openClose', [
      state('init', style({
        maxHeight: '0px',
        paddingTop: '0',
        paddingBottom: '0',
      })),
      state('opened', style({
        maxHeight: '150px',
        paddingTop: '0.75rem',
        paddingBottom: '0.75rem',
      })),
      state('closed', style({
        maxHeight: '0px',
        paddingTop: '0',
        paddingBottom: '0',
      })),
      transition('* => init', [
        animate('0.0s')
      ]),
      transition('init => opened', [
        animate('0.1s')
      ]),
      transition('opened => closed', [
        animate('0.1s')
      ])
    ]),
  ]
})
export class AlertComponent implements OnChanges {
  @Input() alert: Alert;
  list: Alert[] = [];
  id = 0;

  constructor() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!changes.alert.currentValue) {
      return;
    }
    const al: Alert = changes.alert.currentValue;
    al.state = 'init';
    al.id = ++this.id;
    if(!al.duration){
      al.duration = 3;
    }
    this.list.unshift(al);
    if(al.duration>0){
      setTimeout(() => this.remove(al), al.duration*1000);
    }
    setTimeout(() => al.state='opened', 1);
  }

  public remove(a: Alert){
    a.state = 'closed';
  }

  public animEnd(alert: Alert){
    if(alert.state!=='closed'){
      return;
    }
    this.list = this.list.filter((a: Alert, i: number, ar: any) => a.id!==alert.id);
  }

}
