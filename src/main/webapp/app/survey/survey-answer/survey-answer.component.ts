import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISurvey } from 'app/shared/model/survey.model';
import { Challenger } from 'app/shared/model/challenger.model';
import { Answer, IAnswer } from 'app/shared/model/answer.model';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import moment = require('moment');
import { AnswerService } from 'app/entities/answer';
import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-survey-answer',
    templateUrl: './survey-answer.component.html',
    styles: []
})
export class SurveyAnswerComponent implements OnInit {
    survey: ISurvey;
    nbOfMatches: number;
    judgeId: string;
    // winners: Challenger[] = [];
    answers: Answer[] = [];
    challengerOne: Challenger;
    challengerTwo: Challenger;
    isAllMatchesCompleted: boolean = false;
    matchStarts: moment.Moment;

    constructor(private activatedRoute: ActivatedRoute, public sanitizer: DomSanitizer, private answerService: AnswerService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ survey }) => {
            this.survey = survey;
        });

        this.nbOfMatches = this.survey.numberOfMatches;

        //TODO send requests to init images URLs

        this.initNextMatch();

        const uniqueString = require('unique-string');
        this.judgeId = 'judge_' + uniqueString();
    }

    initNextMatch() {
        //TODO add random selection of images
        if (this.challengerOne && this.challengerOne.id == 'GOPR0111_2856-16') {
            this.challengerOne = new Challenger('GOPR0111_0853', '../../content/images/survey/GOPR0111_0853.png');
            this.challengerTwo = new Challenger('GOPR0111_2136', '../../content/images/survey/GOPR0111_2136.png');
        } else {
            this.challengerOne = new Challenger('GOPR0111_2856-16', '../../content/images/survey/GOPR0111_2856-16.png');
            this.challengerTwo = new Challenger('GOPR0111_3042', '../../content/images/survey/GOPR0111_3042.png');
        }

        this.matchStarts = moment();
    }

    addWinner(winner) {
        console.debug('Winner added: ' + JSON.stringify(winner));
        // Add a new Answer from the winner
        let matchEnds = moment();
        this.answers.push(
            new Answer(
                undefined,
                this.judgeId,
                this.challengerOne.id,
                this.challengerTwo.id,
                winner.id,
                this.matchStarts,
                matchEnds,
                this.survey.id
            )
        );

        // Determine whether to display next challengers
        if (this.answers.length < this.nbOfMatches) {
            this.initNextMatch();
        } else {
            // If we've got enought winners, save the answers and display socio-pro questions

            console.debug('Answers: ' + JSON.stringify(this.answers));
            this.isAllMatchesCompleted = true;

            for (let answer of this.answers) {
                //TODO create service to save a list of Answers
                this.subscribeToSaveResponse(this.answerService.create(answer));
            }
        }
    }

    getCurrentMatchNumber(): number {
        return this.answers.length + 1;
    }

    getPercentAdvancement(): number {
        return (this.getCurrentMatchNumber() * 100) / this.nbOfMatches;
    }

    getSocialFormUrl(): SafeResourceUrl {
        return this.sanitizer.bypassSecurityTrustResourceUrl(this.survey.formURL + this.judgeId);
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAnswer>>) {
        result.subscribe((res: HttpResponse<IAnswer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    onSaveError(): void {
        alert('An error occured while saving your answers. Please try again later.');
    }

    onSaveSuccess(): void {
        // alert('Save success!');
    }
}
