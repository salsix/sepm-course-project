import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Show} from '../../dtos/show';
import {Loc} from '../../dtos/loc';
import {Hallplan} from '../../dtos/hallplan';
import {LocationService} from '../../services/location.service';
import {Alert} from '../../classes/alert';
import {BasicError} from '../../classes/basicError';

@Component({
  selector: 'app-show-edit',
  templateUrl: './admin-show-edit.component.html',
  styleUrls: ['./admin-show-edit.component.scss']
})
export class AdminShowEditComponent extends BasicError implements OnInit {
  @Output() alert: EventEmitter<Alert> = new EventEmitter<Alert>();
  @Input() show: Show = null;

  countryList: string[] = [];
  country: string = null;

  zipList: string[] = [];
  zip: string = null;

  streetList: string[] = [];
  street: string = null;

  locationList: Loc[] = [];
  locationId: number = null;

  hallplanList: Hallplan[] = [];
  hallplanId: number = null;

  constructor(private locationService: LocationService) {
    super();
  }

  ngOnInit(): void {
    if(this.show.hallplan){
      const hallplan = this.show.hallplan;
      const loc = hallplan.location;

      this.country = loc.country;
      this.loadCountries();
      this.zip = loc.zip;
      this.loadZips();
      this.street = loc.street;
      this.loadLocations();
      this.locationId = loc.id;
      this.loadHallplans();
      this.hallplanId = hallplan.id;

      this.show.hallplan = hallplan;
    } else {
      this.loadCountries();
    }
  }

  public loadCountries() {
    this.show.hallplan = null;
    this.zip = null;
    this.street = null;
    this.locationId = null;
    this.hallplanId = null;

    this.locationService.listCountries().subscribe(
      (list: string[]) => {
        this.countryList = list;
      },
      error => {
        this.alert.emit({type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1});
      }
    );
  }
  public loadZips() {
    this.show.hallplan = null;
    this.street = null;
    this.locationId = null;
    this.hallplanId = null;

    this.locationService.listZips(this.country).subscribe(
      (list: string[]) => {
        this.zipList = list;
      },
      error => {
        this.alert.emit({type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1});
      }
    );
  }

  public loadLocations() {
    this.show.hallplan = null;
    this.hallplanId = null;

    this.locationService.listLocations(this.country, this.zip).subscribe(
      (list: Loc[]) => {
        this.locationList = list;
      },
      error => {
        this.alert.emit({type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1});
      }
    );
  }
  public loadHallplans(){
    this.show.hallplan = null;

    this.locationService.get(this.locationId).subscribe(
      (location: Loc) => {
        this.hallplanList = location.hallplans;
      },
      error => {
        this.alert.emit({type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1});
      }
    );
  }

  public setHallplan(){
    this.show.hallplan = {id: this.hallplanId, areas: null, cats: null, location: null, name: null, seats: null};
  }

}
