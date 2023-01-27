import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import Label from "../../../../../model/label.model";
import {BehaviorSubject, Subject, take, takeUntil} from "rxjs";
import {FormBuilder, FormGroup} from "@angular/forms";
import {LabelService} from "../../../../../services/label/label.service";
import Project from "../../../../../model/project.model";
import Goal from "../../../../../model/goal.model";

@Component({
  selector: 'app-label-list',
  templateUrl: './label-list.component.html',
  styleUrls: ['./label-list.component.scss']
})
export class LabelListComponent implements OnInit, AfterViewInit, OnDestroy {

  @Input() labels!: Label[];
  @Input() canAddLabel = false;
  @Input() limit!: number;
  @Input() project!: Project;
  @Input() goal!: Goal;
  @Output() labelAdded = new EventEmitter<Label>();
  @Output() labelRemoved = new EventEmitter<Label>();

  @ViewChild("labelNameInput") labelNameInput!: ElementRef<HTMLInputElement>;

  private inputLabelShown$ = new BehaviorSubject(false);
  get isInputLabelShown() {
    return this.inputLabelShown$.asObservable();
  }

  private showAutocomplete$ = new BehaviorSubject(false);
  get isAutoCompleteShown() {
    return this.showAutocomplete$.asObservable();
  }

  private componentDestroyed$ = new Subject();

  autocompleteLabels = [] as Label[];

  form!: FormGroup;

  constructor(
    private _fb: FormBuilder,
    private _labelService: LabelService
  ) { }

  handleAddLabelClick() {
    this.inputLabelShown$.next(true);
    this.autocompleteLabels = [];
  }

  ngOnInit(): void {
    this.form = this._fb.group({
      labelName: this._fb.control('')
    });

    this.form.valueChanges
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(({ labelName }) => {
        this.loadLabels(labelName);
      });
  }

  ngAfterViewInit(): void {
    this.labelNameInput?.nativeElement.focus();
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  loadLabels(labelName: string) {
    if (!labelName) return;

    this._labelService.getLabelByName({
      project_id: this.project.projectId,
      name: labelName
    })
      .pipe(take(1))
      .subscribe(result => {
        this.showAutocomplete$.next(result.length > 0);
        this.autocompleteLabels = result.filter(l => !this.labels.some(l2 => l2.label_id === l.label_id));
      })
  }

  addLabel(label: Label) {
    this.showAutocomplete$.next(false);
    this.inputLabelShown$.next(false);
    this.autocompleteLabels = [];
    this.labelAdded.emit(label);
  }

  handleCloseLabelClick() {
    this.inputLabelShown$.next(false);
    this.showAutocomplete$.next(false);
    this.autocompleteLabels = [];
  }

  handleUnassignLabel($event: Label) {
    this.labelRemoved.emit($event);
  }

  handleFocus() {
    this._labelService.labelOfProject(this.project.projectId)
      .pipe(take(1))
      .subscribe(labels => {
        this.autocompleteLabels = labels.filter(l => !this.labels.some(l2 => l2.label_id === l.label_id));
        this.showAutocomplete$.next(this.autocompleteLabels.length > 0);
      });
  }
}
