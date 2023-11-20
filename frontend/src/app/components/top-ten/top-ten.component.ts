import { Component, OnInit } from '@angular/core';
import {BasicError} from '../../classes/basicError';
import {ActivatedRoute, Router} from '@angular/router';
import {EventService} from '../../services/event.service';
import {TopTen} from '../../dtos/top-ten';

@Component({
  selector: 'app-top-ten',
  templateUrl: './top-ten.component.html',
  styleUrls: ['./top-ten.component.scss']
})
export class TopTenComponent extends BasicError implements OnInit {
  category = 'Movie';
  topTen: TopTen[];
  categories: string[];

  constructor(private route: ActivatedRoute, private router: Router, private eventService: EventService) {
    super(router);
  }

  ngOnInit(): void {
    this.eventService.getCategories().subscribe(
      (categories: string[]) => {
        this.categories = categories;
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );

    this.route.queryParams.subscribe( paramMap => {
      this.category = paramMap.category;
      if(!paramMap.category){
        this.category = 'Movie';
      }
      this.load();
    });
  }

  load(){
    this.eventService.topten(this.category).subscribe(
      (topTen: TopTen[]) => {
        this.topTen = topTen;
      },
      error => {
        this.basicAlert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }

  public imagePath(nr: number): string {
    return this.eventService.imagePath(nr);
  }

  public changeCategory(category: string){
    if(this.category===category){
      return;
    }
    this.category = category;
    this.router.navigate(['.'], {relativeTo: this.route, queryParams: {category}, queryParamsHandling: 'merge'}).then(() => {});
  }

}
