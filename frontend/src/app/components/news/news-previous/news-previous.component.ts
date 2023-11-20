import {Component, OnInit} from '@angular/core';
import {Alert} from '../../../classes/alert';
import {News} from '../../../dtos/news';
import {NewsService} from '../../../services/news.service';
import {BasicError} from '../../../classes/basicError';

@Component({
  selector: 'app-news-previous',
  templateUrl: './news-previous.component.html',
  styleUrls: ['./news-previous.component.scss']
})
export class NewsPreviousComponent extends BasicError implements OnInit {
  alert: Alert;

  id: number;
  newsEntry: News;
  image: File;
  imageList: string[];
  submitting = false;
  newsTitle: string;
  newsShortDescription: string;
  newsReleaseDate: string;
  selectedNews: News;

  constructor(private newsService: NewsService) {
    super();
  }

  loadAllPreviousNews() {
    // @ts-ignore
    this.newsService.getPreviousNews().subscribe((nextNews: News) => {
        this.newsEntry = nextNews;
        this.newsTitle = null;
        this.newsShortDescription = null;
        this.newsReleaseDate = null;
      },
      error => {
        console.log(error);
        this.alert = {
          type: 'danger',
          strongMessage: 'Error: ',
          message: this.getErrorMessage(error),
          duration: -1
        };
      }
    );
  }

  ngOnInit(): void {
    this.loadAllPreviousNews();
  }
}
