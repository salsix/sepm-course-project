import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnChanges {
  @Input() canChange = true;
  @Input() maxPage = 1000000000;
  @Input() inPage;
  @Output() page: EventEmitter<number> = new EventEmitter<number>();

  internPage = 1;

  constructor() { }

  ngOnChanges(changes: SimpleChanges): void {
    if (!changes.inPage || !changes.inPage.currentValue) {
      return;
    }
    const page: number = changes.inPage.currentValue + 1;
    if(page!==this.internPage){
      this.internPage = page;
      this.capPage();
    }
  }

  public change(event: Event){
    this.internPage = +event.target['value'];
    this.capPage();
    this.emitPage();
  }

  public add(add: number) {
    this.internPage += add;
    this.capPage();
    this.emitPage();
  }

  public emitPage(){
    this.page.emit(this.internPage);
    this.capPage();
  }

  private capPage(){
    if(!this.internPage){
      this.internPage = 1;
    }
    if(this.internPage<1){
      this.internPage = 1;
    }
    if(this.internPage>this.maxPage){
      this.internPage = this.maxPage;
    }

  }
}
