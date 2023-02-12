import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'vp-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent {

  @Input() title!: string;
  @Input() hasError = false;

  @Output() uploaded = new EventEmitter<File | undefined>();

  @ViewChild("input") input!: ElementRef<HTMLInputElement>;

  private fileUploaded$ = new BehaviorSubject(false);
  get isFileUploaded() {
    return this.fileUploaded$.asObservable();
  }

  handleFileInputChange($event: Event) {
    const file = ($event.target as HTMLInputElement)?.files?.[0];
    if (!file) {
      this.fileUploaded$.next(false);
    }
    this.handleFileUpload(file);
  }

  handleFileDropped($event: File | undefined) {
    this.input.nativeElement.value = "";
    this.input.nativeElement.files = null;
    this.handleFileUpload($event);
  }

  handleFileUpload($event: File | undefined) {
    this.fileUploaded$.next(!!$event);
    this.uploaded.emit($event)
  }
}
