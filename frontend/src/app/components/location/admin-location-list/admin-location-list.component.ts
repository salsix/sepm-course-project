import { Component, OnInit } from '@angular/core';
import {BasicError} from '../../../classes/basicError';
import {Loc} from '../../../dtos/loc';
import {LocationService} from '../../../services/location.service';
import {Alert} from '../../../classes/alert';

@Component({
  selector: 'app-location-list',
  templateUrl: './admin-location-list.component.html',
  styleUrls: ['./admin-location-list.component.scss']
})
export class AdminLocationListComponent extends BasicError implements OnInit {
  alert: Alert;
  locations: Loc[];
  newLoc: Loc;
  defaultLoc: Loc = {id: null, name: '', country: '', city: '', street: '', zip: '', hallplans: []};
  submitting = false;

  constructor(private locationService: LocationService) {
    super();
  }

  ngOnInit(): void {
    this.newLoc = {...this.defaultLoc};
    this.locationService.getAll().subscribe(
      (l: Loc[]) => {
        this.locations = l.sort((a: Loc, b: Loc) => a.name.localeCompare(b.name));
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public saveLocation(){
    this.submitting = true;
    this.locationService.save(this.newLoc).subscribe(
      (l: Loc) => {
        this.locations.push(l);
        this.newLoc = { ...this.defaultLoc };
        this.submitting = false;
        this.alert = {type: 'success', strongMessage: 'Success: ', message: 'saved new location'};
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: 8};
        this.submitting = false;
      }
    );
  }



}
