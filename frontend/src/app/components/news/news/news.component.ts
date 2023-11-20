import {Component, EventEmitter, Input, Output} from '@angular/core';
import {News} from '../../../dtos/news';
import {NewsService} from '../../../services/news.service';
import {Alert} from '../../../classes/alert';
import {BasicError} from '../../../classes/basicError';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent extends BasicError{
  @Output() imageEvent: EventEmitter<File[]> = new EventEmitter<File[]>();
  alert: Alert;
  defaultNews: News = {id: null, title: '', shortDescription: '', content: '', publishedAt: null , alreadyRead: false, imageCount:0};
  newNews: News = {...this.defaultNews};
  submitting = false;
  images: File[];
  constructor(private newsService: NewsService) {
    super();
  }
  outputImage(news: Event){
    const files: File[] = news.target['files'];
    this.imageEvent.emit(files);
    this.images = files;
  }
  public saveNews(){
    this.submitting = true;
    this.newsService.save(this.newNews).subscribe(
      (n: News) => {
        if(!this.images){
          this.newFinish();
          this.submitting = false;
          this.alert = {type: 'success', strongMessage: 'Success: ', message: 'Saved new news'};
          return;
        }

        //save image
        this.newsService.saveImages(n.id, this.images).subscribe(
          () => {
            this.newFinish();
            this.submitting = false;
            this.alert = {type: 'success', strongMessage: 'Success: ', message: 'Saved new news'};
          },
          error => {
            this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};

            this.newsService.delete(n.id).subscribe(
              () => {
                this.submitting = false;
              },
              error2 => {
                this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error2)};
                this.submitting = false;
              }
            );
          }
        );
      },
      error => {
        this.alert = {type: 'danger', strongMessage: 'Error: ', message: this.getErrorMessage(error)};
        this.submitting = false;
      }
    );
  }
  private newFinish(){
    this.newNews = { ...this.defaultNews };
  }

}
