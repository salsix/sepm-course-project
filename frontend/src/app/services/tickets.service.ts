import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Booked} from '../dtos/booked';
import {Bill} from '../dtos/bill';

@Injectable({
  providedIn: 'root'
})
export class TicketsService {
  private messageBaseUri: string = this.globals.backendUri + '/tickets';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  public zeropad(nr: number, count: number): string {
    let str = ''+nr;
    while(str.length<count){
      str = '0'+str;
    }
    return str;
  }

  public timeString(hour: number, minute: number): string {
    return this.zeropad(hour, 2) + ':' + this.zeropad(minute, 2);
  }

  public dateString(date: Date): string {
    return date.getDate()+'/'+(date.getMonth()+1)+'/'+date.getFullYear();
  }

  public dateStringFromString(dateString: string): string {
    const date: Date = new Date(dateString);
    return date.getDate()+'/'+(date.getMonth()+1)+'/'+date.getFullYear();
  }

  buyTickets(showId: number, booked: Booked): Observable<number> {
    console.log('buy', booked);
    return this.httpClient.post<number>(this.messageBaseUri+'/buy', booked);
  }

  reserveTickets(showId: number, booked: Booked): Observable<number> {
    console.log('reserve', booked);
    return this.httpClient.post<number>(this.messageBaseUri+'/reserve', booked);
  }

  buyReservedTickets(showId: number, billId: number, booked: Booked): Observable<number> {
    console.log('buy reserved', booked);
    return this.httpClient.post<number>(this.messageBaseUri+'/'+billId+'/buy', booked);
  }

  stornoTickets(billId: number): Observable<any> {
    console.log('storno', billId);
    return this.httpClient.delete<any>(this.messageBaseUri+'/'+billId);
  }

  /**
   * Loads a bill from the backend
   */
  getBill(billId: number): Observable<Bill> {
    return this.httpClient.get<Bill>(this.messageBaseUri+'/'+billId);
  }

  getUserTickets(): Observable<Bill[]> {
    console.log('getUserTickets');
    return this.httpClient.get<Bill[]>(this.messageBaseUri+'');
  }

  getOldUserTickets(page: number): Observable<Bill[]> {
    console.log('getOldUserTickets');
    return this.httpClient.get<Bill[]>(this.messageBaseUri+'/past?page='+page);
  }

  getOrderPdf(billId: number): Observable<Blob> {
    const uri = this.messageBaseUri+'/'+billId+'/orderPdf';
    return this.httpClient.get(uri, { responseType: 'blob' });
  }
  getBillPdf(billId: number): Observable<Blob> {
    const uri = this.messageBaseUri+'/'+billId+'/billPdf';
    return this.httpClient.get(uri, { responseType: 'blob' });
  }
  getStornoBillPdf(billId: number): Observable<Blob> {
    const uri = this.messageBaseUri+'/'+billId+'/stornoBillPdf';
    return this.httpClient.get(uri, { responseType: 'blob' });
  }
}
