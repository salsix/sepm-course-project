import { Component, OnInit } from '@angular/core';
import {News} from '../../../dtos/news';
import {ActivatedRoute} from '@angular/router';
import {NewsService} from '../../../services/news.service';
import {Alert} from '../../../classes/alert';
import {BasicError} from '../../../classes/basicError';

@Component({
  selector: 'app-news-details',
  templateUrl: './news-details.component.html',
  styleUrls: ['./news-details.component.scss']
})
export class NewsDetailsComponent extends BasicError implements OnInit {
  alert: Alert;

  id: number;
  news: News;
  image: File;
  imageList: string[];
  submitting = false;
  constructor(private route: ActivatedRoute, private newsService: NewsService) {
    super();
  }
  ngOnInit(): void {
    this.route.paramMap.subscribe( paramMap => {
      this.id = +paramMap.get('id');
      this.loadEvent();
      this.setNewsRead(this.id).subscribe();
    });
  }

  public loadEvent(){
    this.newsService.getById(this.id).subscribe(
      (n: News) => {
        console.log(n);
        this.news = n;
        this.imageList = this.imagePathMulti();
        this.id = n.id;
      },
      error => {
        console.log(error);
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error), duration: -1};
      }
    );
  }
  public setNewsRead(newsId: number) {
    return this.newsService.setNewsRead(newsId);
  }
  public imagePath(): string {
    return this.newsService.imagePath(this.news.id);
  }
  public imagePathMulti(): string[] {
    return this.newsService.imagePathMulti(this.news.id,this.news.imageCount);
  }
}
