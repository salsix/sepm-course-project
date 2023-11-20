import {Component, Input} from '@angular/core';
import {Loc} from '../../../dtos/loc';

@Component({
  selector: 'app-location-edit',
  templateUrl: './admin-location-edit.component.html',
  styleUrls: ['./admin-location-edit.component.scss']
})
export class AdminLocationEditComponent {
  @Input() location: Loc;
  constructor() { }
}
