import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscoverMainComponent } from './discover-main.component';

describe('DiscoverMainComponent', () => {
  let component: DiscoverMainComponent;
  let fixture: ComponentFixture<DiscoverMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DiscoverMainComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DiscoverMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
