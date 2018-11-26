/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BiodifulTestModule } from '../../../test.module';
import { AnswerDeleteDialogComponent } from 'app/entities/answer/answer-delete-dialog.component';
import { AnswerService } from 'app/entities/answer/answer.service';

describe('Component Tests', () => {
    describe('Answer Management Delete Component', () => {
        let comp: AnswerDeleteDialogComponent;
        let fixture: ComponentFixture<AnswerDeleteDialogComponent>;
        let service: AnswerService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BiodifulTestModule],
                declarations: [AnswerDeleteDialogComponent]
            })
                .overrideTemplate(AnswerDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnswerDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnswerService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
