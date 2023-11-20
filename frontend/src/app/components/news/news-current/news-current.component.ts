import {Component, OnInit} from '@angular/core';
import {BasicError} from '../../../classes/basicError';
import {NewsService} from '../../../services/news.service';
import {Alert} from '../../../classes/alert';
import {News} from '../../../dtos/news';
import {Router} from '@angular/router';


@Component({
  selector: 'app-news-current',
  templateUrl: './news-current.component.html',
  styleUrls: ['./news-current.component.scss']
})
export class NewsCurrentComponent extends BasicError implements OnInit {
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

  constructor(private newsService: NewsService, private router: Router) {
    super(router);
  }

  loadAllCurrentNews() {
    // @ts-ignore
    this.newsService.getAllCurrentNews().subscribe((nextNews: News) => {
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
    this.loadAllCurrentNews();
  }
}
