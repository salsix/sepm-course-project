import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {SearchService} from '../../../services/search.service';
import {Even} from '../../../dtos/even';
import {BasicError} from '../../../classes/basicError';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-event-search',
  templateUrl: './event-search.component.html',
  styleUrls: ['./event-search.component.scss']
})
export class EventSearchComponent extends BasicError implements OnInit {
  eventSearchValid = true;
  eventSearchForm = new FormGroup({
    search: new FormControl('')
  });
  events: Even[] = [];

  pageOld = 0;
  page = 0;
  pageLoad = false;

  constructor(private searchService: SearchService, private route: ActivatedRoute, private router: Router) {
    super(router);
  }

  ngOnInit(): void {
  }

  public pagination(page: number) {
    this.pageLoad = true;
    this.pageOld = this.page;

    this.page = page - 1;

    if (this.page === this.pageOld) {
      this.pageLoad = false;
      return;
    }

    this.loadEvents();
  }

  public loadEvents() {
    const searchTerms = this.eventSearchForm.value.search.split(' ');
    if (searchTerms.length > 5) {
      this.eventSearchValid = false;
    } else {
      this.searchService.eventSearch(searchTerms, this.page).subscribe(
        (result: Even[]) => {
          if (result.length === 0) {
            if(this.events.length > 0){
              this.basicAlert = {type: 'warning', strongMessage: '', message: 'No more events', duration: 2};
              this.page = this.pageOld;
              this.pageLoad = false;
            } else {
              this.basicAlert = {type: 'warning', strongMessage: '', message: 'No events found', duration: 2};
            }
            return;
          }
          this.events = result;
          this.pageLoad = false;
        },
        error => {
          this.basicAlert = {
            type: 'danger',
            strongMessage: 'Error: ',
            message: this.getErrorMessage(error),
            duration: -1
          };
          this.pageLoad = false;
        }
      );
    }
  }
}
