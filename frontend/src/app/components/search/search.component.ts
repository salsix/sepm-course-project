import { Component, OnInit } from '@angular/core';
import {FormGroup, FormControl, ValidatorFn, AbstractControl, ValidationErrors} from '@angular/forms';
import {SearchService} from '../../services/search.service';
import {Even} from '../../dtos/even';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  eventSearchValid = true;
  eventSearchForm = new FormGroup({
    search: new FormControl('')
  });

  constructor(private searchService: SearchService) { }

  ngOnInit(): void {
  }

}
