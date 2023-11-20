import { Component, OnInit } from '@angular/core';
import {BasicError} from '../../classes/basicError';
import {ActivatedRoute, Router} from '@angular/router';
import {Even} from '../../dtos/even';
import {EventService} from '../../services/event.service';
import {ShowService} from '../../services/show.service';
import {TicketsService} from '../../services/tickets.service';
import {Show} from '../../dtos/show';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent extends BasicError implements OnInit {
  id: number;
  event: Even;

  constructor(private route: ActivatedRoute, private router: Router,
              private eventService: EventService, public ticketsService: TicketsService) {
    super(router);
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe( paramMap => {
      this.id = +paramMap.get('id');
      this.load();
    });
  }


  public load(){
    this.eventService.getById(this.id).subscribe(
      (e: Even) => {
        console.log(e);
        //sort
        e.shows = e.shows.filter(ShowService.showInFuture);
        e.shows = e.shows.sort(ShowService.sortShows);
        this.event = e;
      },
      error => {
        console.log(error);
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public imagePath(): string {
    return this.eventService.imagePath(this.id);
  }

}
