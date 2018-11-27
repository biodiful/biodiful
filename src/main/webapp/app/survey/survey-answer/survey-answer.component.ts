import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISurvey } from 'app/shared/model/survey.model';
import { Challenger } from 'app/shared/model/challenger.model';
import { Answer } from 'app/shared/model/answer.model';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import moment = require('moment');

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

    constructor(private activatedRoute: ActivatedRoute, public sanitizer: DomSanitizer) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ survey }) => {
            this.survey = survey;
        });

        this.nbOfMatches = this.survey.numberOfMatches;

        //TODO send requests to init images URLs

        this.initNextMatch();

        this.judgeId =
            'judge_' +
            Math.random()
                .toString(36)
                .substr(2, 9);
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

        //TODO initialise matchStartsMoment
    }

    addWinner(winner) {
        console.log('Winner added: ' + JSON.stringify(winner));
        // Add a new Answer from the winner
        let matchEndsMoment = moment();
        this.answers.push(
            new Answer(
                undefined,
                this.judgeId,
                this.challengerOne.id,
                this.challengerTwo.id,
                winner.id,
                matchEndsMoment,
                matchEndsMoment,
                this.survey.id
            )
        );

        // Determine whether to display next challengers
        if (this.answers.length < this.nbOfMatches) {
            this.initNextMatch();
        } else {
            // if we've got enought winners, display socio-pro questions
            console.log('Answers:');
            console.log(JSON.stringify(this.answers));
            this.isAllMatchesCompleted = true;
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
}
