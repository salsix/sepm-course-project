import { Component, OnInit } from '@angular/core';
import {ShowService} from '../../services/show.service';
import {Show} from '../../dtos/show';
import {ActivatedRoute} from '@angular/router';
import {Alert} from '../../classes/alert';
import {BasicError} from '../../classes/basicError';

@Component({
  selector: 'app-show',
  templateUrl: './admin-show.component.html',
  styleUrls: ['./admin-show.component.scss']
})
export class AdminShowComponent extends BasicError implements OnInit {
  alert: Alert;

  id: number;
  show: Show;
  submitting = false;
  newShowName = '';

  constructor(private route: ActivatedRoute, private showService: ShowService) {
    super();
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe( paramMap => {
      this.id = +paramMap.get('id');
      this.loadShow();
    });
  }

  public loadShow(){
    this.showService.getById(this.id).subscribe(
      (s: Show) => {
        console.log(s);
        //e.hallplans = s.hallplans.sort((a: Hallplan, b: Hallplan) => a.name.localeCompare(b.name));
        //TODO: alphabetisch sortieren? Oder nach Datum?
        this.show = s;
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public editShow(){
    this.submitting = true;
    this.showService.edit(this.show).subscribe(
      () => {
        this.submitting = false;
        this.alert = {type: 'success', strongMessage: 'Success: ', message: 'saved show'};
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};
        this.submitting = false;
      }
    );
  }


}
