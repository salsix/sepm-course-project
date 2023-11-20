import { Injectable } from '@angular/core';
import {observable, Observable} from 'rxjs';
import {News} from '../dtos/news';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  private messageBaseUri: string = this.globals.backendUri + '/news';
  constructor(private httpClient: HttpClient, private globals: Globals) { }
  /**
   * Loads all News from the backend
   */
  getAllNews(): Observable<News[]> {
    return this.httpClient.get<News[]>(this.messageBaseUri);
  }
  getAllCurrentNews(): Observable<News[]> {
    return this.httpClient.get<News[]>(this.messageBaseUri + '/current');
  }
  getPreviousNews(): Observable<News[]> {
    return this.httpClient.get<News[]>(this.messageBaseUri + '/previous');
  }
  setNewsRead(id: number): Observable<News>{
    console.log('Set News read ' + id);
    console.log(this.messageBaseUri +  '/' + id + '/read');
    return this.httpClient.get<News>(this.messageBaseUri +  '/' + id + '/read' , {});
  }
  /**
   * Loads specific News from the backend
   *
   * @param id of News to load
   */
  getById(id: number): Observable<News> {
    console.log('Load News details for ' + id);
    return this.httpClient.get<News>(this.messageBaseUri + '/' + id);
  }

  /**
   * Persists News to the backend
   *
   * @param news
   */
  save(news: News): Observable<News> {
    console.log('save news', news);
    return this.httpClient.post<News>(this.messageBaseUri, news);
  }
  saveImages(newsId: number, images: File[]): Observable<any> {
    console.log(images);
    console.log('image', newsId, images.length);
    const formData = new FormData();
    for (const item of images) {
      formData.append('images', item, item.name);
    }
    return this.httpClient.post<any>(this.imagePath(newsId), formData);
  }
  delete(id: number): Observable<any> {
    console.log('delete news', id);
    return this.httpClient.delete<any>(this.messageBaseUri+'/'+id);
  }

  imagePath(newsId: number): string {
    return this.messageBaseUri+'/'+newsId+'/images/';
  }
  imagePathMulti(newsId: number,imgCount: number): string[] {
    const images = new Array(imgCount);
    for (let i = 0; i <= imgCount-1; i++) {
      images[i] = this.messageBaseUri+'/'+newsId+'/images/'+i;
    }
    return images;
  }
}
