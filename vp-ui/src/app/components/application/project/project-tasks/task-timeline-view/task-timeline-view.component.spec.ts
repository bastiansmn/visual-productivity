import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskTimelineViewComponent } from './task-timeline-view.component';

describe('TaskTimelineViewComponent', () => {
  let component: TaskTimelineViewComponent;
  let fixture: ComponentFixture<TaskTimelineViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TaskTimelineViewComponent]
    });
    fixture = TestBed.createComponent(TaskTimelineViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
