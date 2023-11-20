import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {EventService} from '../../services/event.service';
import {Even} from '../../dtos/even';
import {Show} from '../../dtos/show';
import {ShowService} from '../../services/show.service';
import {Alert} from '../../classes/alert';
import {BasicError} from '../../classes/basicError';

@Component({
  selector: 'app-event',
  templateUrl: './admin-event.component.html',
  styleUrls: ['./admin-event.component.scss']
})
export class AdminEventComponent extends BasicError implements OnInit {
  alert: Alert;

  id: number;
  event: Even;
  image: File;
  submitting = false;
  show: Show = {id: null, event: null, date: '', hour: 14, minute: 30, hallplan: null};
  showList: Show[] = [];

  constructor(private route: ActivatedRoute, private eventService: EventService, private showService: ShowService) {
    super();
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe( paramMap => {
      this.id = +paramMap.get('id');
      this.loadEvent();
    });
  }

  public loadEvent(){
    this.eventService.getById(this.id).subscribe(
      (e: Even) => {
        console.log(e);
        //e.shows = e.shows.sort((a: Show, b: Show) => a.name.localeCompare(b.name));
        //TODO: alphabetisch sortieren? Oder nach Datum?
        this.event = e;
      },
      error => {
        console.log(error);
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public editEvent(){
    this.submitting = true;
    this.eventService.edit(this.event).subscribe(
      () => {
        if(!this.image){
          this.submitting = false;
          this.alert = {type: 'success', strongMessage: 'Success: ', message: 'saved event'};
          return;
        }

        //save image
        this.eventService.image(this.event.id, this.image).subscribe(
          () => {
            this.submitting = false;
            this.alert = {type: 'success', strongMessage: 'Success: ', message: 'saved event'};
          },
          error => {
            this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};
            this.submitting = false;
          }
        );
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};
        this.submitting = false;
      }
    );
  }

  public newShow(){
    this.showService.save(this.event.id, this.show).subscribe(
      (s: Show) => {
        this.submitting = false;
        this.event.shows.push(s);
        this.alert = {type: 'success', strongMessage: 'Success: ', message: 'saved new show'};
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};
        this.submitting = false;
      }
    );
  }

  public imagePath(): string {
    return this.eventService.imagePath(this.event.id);
  }
}
