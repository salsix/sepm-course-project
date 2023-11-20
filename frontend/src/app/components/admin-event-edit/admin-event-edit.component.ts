import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Even} from '../../dtos/even';
import {EventService} from '../../services/event.service';
import {Alert} from '../../classes/alert';
import {BasicError} from '../../classes/basicError';

@Component({
  selector: 'app-event-edit',
  templateUrl: './admin-event-edit.component.html',
  styleUrls: ['./admin-event-edit.component.scss']
})
export class AdminEventEditComponent extends BasicError {
  @Input() event: Even = null;
  @Output() image: EventEmitter<File> = new EventEmitter<File>();
  @Output() alert: EventEmitter<Alert> = new EventEmitter<Alert>();

  categoryList: string[] = [];

  constructor(private eventService: EventService) {
    super();
  }

  outputImage(event: Event){
    const files: File[] = event.target['files'];
    this.image.emit(files[0]);
  }

  public loadCategories(){
    console.log('Load categories');
    this.eventService.getCategories().subscribe(
      (list: string[]) => {
        this.categoryList = list;
      },
      error => {
        this.alert.emit({type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1});
      }
    );
  }


}
