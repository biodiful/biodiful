/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { BiodifulTestModule } from '../../../test.module';
import { AnswerUpdateComponent } from 'app/entities/answer/answer-update.component';
import { AnswerService } from 'app/entities/answer/answer.service';
import { Answer } from 'app/shared/model/answer.model';

describe('Component Tests', () => {
    describe('Answer Management Update Component', () => {
        let comp: AnswerUpdateComponent;
        let fixture: ComponentFixture<AnswerUpdateComponent>;
        let service: AnswerService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BiodifulTestModule],
                declarations: [AnswerUpdateComponent]
            })
                .overrideTemplate(AnswerUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AnswerUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnswerService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Answer(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.answer = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Answer();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.answer = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
