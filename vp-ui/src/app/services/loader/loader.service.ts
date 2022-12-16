import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoaderService {

  shown: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor() { }

  show() {
    this.shown.next(true);
  }

  hide() {
    this.shown.next(false);
  }

  get isShown() {
    return this.shown;
  }
}
