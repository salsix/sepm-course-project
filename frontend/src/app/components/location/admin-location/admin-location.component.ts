import { Component, OnInit } from '@angular/core';
import {BasicError} from '../../../classes/basicError';
import {Loc} from '../../../dtos/loc';
import {LocationService} from '../../../services/location.service';
import {ActivatedRoute} from '@angular/router';
import {HallplanService} from '../../../services/hallplan.service';
import {Hallplan} from '../../../dtos/hallplan';
import {Alert} from '../../../classes/alert';

@Component({
  selector: 'app-location',
  templateUrl: './admin-location.component.html',
  styleUrls: ['./admin-location.component.scss']
})
export class AdminLocationComponent extends BasicError implements OnInit {
  alert: Alert;
  id: number;
  location: Loc;
  submitting = false;
  newHallName = '';

  constructor(private route: ActivatedRoute, private locationService: LocationService, private hallplanService: HallplanService) {
    super();
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe( paramMap => {
      this.id = +paramMap.get('id');
      this.loadLocation();
    });
  }

  public loadLocation(){
    this.locationService.get(this.id).subscribe(
      (l: Loc) => {
        console.log(l);
        l.hallplans = l.hallplans.sort((a: Hallplan, b: Hallplan) => a.name.localeCompare(b.name));
        this.location = l;
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public editLocation(){
    this.submitting = true;
    this.locationService.edit(this.location).subscribe(
      (l: Loc) => {
        this.submitting = false;
        this.alert = {type: 'success', strongMessage: 'Success: ', message: 'saved location'};
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};
        this.submitting = false;
      }
    );
  }

  public delete(index: number, id: number){
    this.hallplanService.delete(id).subscribe(
      () => {
        console.log('deleted');
        this.alert = {type: 'success', strongMessage: 'Success: ', message: 'deleted hallplan'};
        this.location.hallplans.splice(index, 1);
        this.submitting = false;
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};
        this.submitting = false;
      }
    );
  }

  public newHallplan(){
    this.submitting = true;
    this.hallplanService.save(this.location.id, {
      id: null,
      name: this.newHallName,
      //location: {id: this.location.id, hallplans: null, street: null, zip: null, country: null, name: null},
      location: null,
      cats: [], areas: [], seats: []
    }).subscribe(
      (h: Hallplan) => {
        this.location.hallplans.push(h);
        this.alert = {type: 'success', strongMessage: 'Success: ', message: 'added hallplan'};
        console.log(h);
        this.submitting = false;
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};
        this.submitting = false;
      }
    );
  }

}
