import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {

  shown: boolean = false;

  constructor() { }

  show() {
    this.shown = true;
  }

  hide() {
    this.shown = false;
  }

  get isShown() {
    return this.shown;
  }
}
