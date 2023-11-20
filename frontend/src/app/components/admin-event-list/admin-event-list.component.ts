import { Component, OnInit } from '@angular/core';
import {Even} from '../../dtos/even';
import {EventService} from '../../services/event.service';
import {Alert} from '../../classes/alert';
import {BasicError} from '../../classes/basicError';

@Component({
  selector: 'app-event-list',
  templateUrl: './admin-event-list.component.html',
  styleUrls: ['./admin-event-list.component.scss']
})
export class AdminEventListComponent extends BasicError implements OnInit {
  events: Even[];
  defaultEvent: Even = {id: null, name: '', duration: 0, description: '', shows: [], artists: '', category: {category: ''}};
  newEvent: Even = {...this.defaultEvent};
  submitting = false;
  image: File;
  category = '';

  constructor(private eventService: EventService) {
    super();
  }

  ngOnInit(): void {
    this.eventService.getAll().subscribe(
      (e: Even[]) => {
        console.log(e);
        this.events = e.sort((a: Even, b: Even) => a.name.localeCompare(b.name));
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public saveEvent(){
    this.submitting = true;
    this.eventService.save(this.newEvent).subscribe(
      (e: Even) => {
        if(!this.image){
          this.newFinish(e);
          this.submitting = false;
          this.basicAlert = {type: 'success', strongMessage: 'Success: ', message: 'saved new event'};
          return;
        }

        //save image
        this.eventService.image(e.id, this.image).subscribe(
          () => {
            this.newFinish(e);
            this.submitting = false;
            this.basicAlert = {type: 'success', strongMessage: 'Success: ', message: 'saved new event'};
          },
          error => {
            this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};
            this.submitting = false;
          }
        );
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};
        this.submitting = false;
      }
    );
  }

  public saveCategory(){
    this.submitting = true;
    this.eventService.addCategory(this.category).subscribe(
      () => {
        this.submitting = false;
        this.basicAlert = {type: 'success', strongMessage: 'Success: ', message: 'Category added!', duration: 3};
      },
      error => {
        this.submitting = false;
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  private newFinish(e: Even){
    this.events.push(e);
    this.newEvent = { ...this.defaultEvent };
  }

}
