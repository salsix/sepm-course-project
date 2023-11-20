import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {BasicError} from '../../../classes/basicError';
import {SearchService} from '../../../services/search.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Show} from '../../../dtos/show';
import {TicketsService} from '../../../services/tickets.service';
import {ShowService} from '../../../services/show.service';

@Component({
  selector: 'app-show-search',
  templateUrl: './show-search.component.html',
  styleUrls: ['./show-search.component.scss']
})
export class ShowSearchComponent extends BasicError implements OnInit {
  showSearchForm = new FormGroup({
    maxPrice: new FormControl(''),
    date: new FormControl(''),
    earliestH: new FormControl('0'),
    latestH: new FormControl('23'),
    earliestM: new FormControl('0'),
    latestM: new FormControl('59')
  });
  shows: Show[] = [];

  pageOld = 0;
  page = 0;
  pageLoad = false;

  constructor(
    public ticketsService: TicketsService,
    private searchService: SearchService,
    private route: ActivatedRoute,
    private router: Router
  ) {
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

    this.loadShows();
  }

  public loadShows() {
    this.searchService.showSearch(this.showSearchForm, this.page).subscribe(
      (result: Show[]) => {
        if (result.length === 0) {
          if(this.shows.length > 0){
            this.basicAlert = {type: 'warning', strongMessage: '', message: 'No more shows', duration: 2};
            this.page = this.pageOld;
            this.pageLoad = false;
          } else {
            this.basicAlert = {type: 'warning', strongMessage: '', message: 'No shows found', duration: 2};
          }
          return;
        }
        this.shows = result;
        this.pageLoad = false;
        console.log(result);
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

  public convertInput() {

  }

  public showInFuture(show: Show): boolean {
    return ShowService.showInFuture(show);
  }

}
