import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISurvey } from 'app/shared/model/survey.model';
import { Challenger } from 'app/shared/model/challenger.model';
import { Answer, IAnswer } from 'app/shared/model/answer.model';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import moment from 'moment/src/moment';
import { AnswerService } from 'app/entities/answer';
import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse, HttpClient } from '@angular/common/http';

interface FlickrResponse {
    photoset: Object;
    stat: string;
}

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
    challengers: Challenger[] = [];
    challengerOne: Challenger;
    challengerTwo: Challenger;
    isAllMatchesCompleted: boolean = false;
    matchStarts: moment.Moment;

    flickrResponse: FlickrResponse;

    constructor(
        private activatedRoute: ActivatedRoute,
        public sanitizer: DomSanitizer,
        private answerService: AnswerService,
        private http: HttpClient
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ survey }) => {
            this.survey = survey;
        });

        this.initNbOfMatches();

        this.initChallangers();

        // this.initNextMatch();

        this.initJudgeId();
    }

    initChallangers() {
        // this.survey.challengersURL;
        // let flickrUrl = "https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key=7389b9b323b67e62de25f945af7ccaa3&user_id=162158790@N04&photoset_id=72157697901898520&format=json&nojsoncallback=1";

        // let result = this.http.get(flickrUrl);
        // console.debug("Flickr GET result: " + JSON.stringify(result))

        // this.http.get<FlickrResponse[]>(flickrUrl).map(data => _.values(data))
        // .do(console.log);

        // this.http.get(flickrUrl).subscribe((data: FlickrResponse) => this.flickrResponse = {
        //     photoset: data['photoset'],
        //     stat:  data['stat']
        // });

        this.http.get(this.survey.challengersURL).subscribe(response => this.onGetChallengersSuccess(response), () => this.onError());
    }

    onGetChallengersSuccess(response) {
        console.debug(JSON.stringify(response));
        // for (let photo of response.photoset.photo) {
        let photoset = response['photoset'];
        // for (let photo of photoset["photo"]) {
        for (let photo of photoset.photo) {
            let photoUrl = `https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg`;
            this.challengers.push(new Challenger(photo.title, photoUrl));
        }

        this.initNextMatch();
    }

    initNbOfMatches() {
        this.nbOfMatches = this.survey.numberOfMatches;
        if (this.nbOfMatches < 1) {
            this.nbOfMatches = 10;
        }
    }

    initJudgeId() {
        const uniqueString = require('unique-string');
        this.judgeId = 'judge_' + uniqueString();
    }

    initNextMatch() {
        // Load 2 random chanllengers
        this.challengerOne = this.challengers[Math.floor(Math.random() * this.challengers.length)];
        this.challengerTwo = this.challengers[Math.floor(Math.random() * this.challengers.length)];
        this.checkChallengersAreDifferent();

        this.matchStarts = moment();
    }

    checkChallengersAreDifferent() {
        while (this.challengerOne.id == this.challengerTwo.id) {
            this.challengerTwo = this.challengers[Math.floor(Math.random() * this.challengers.length)];
        }
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
                //TODO create service to save a list of Answers in one call
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
        result.subscribe((res: HttpResponse<IAnswer>) => this.onSaveResponseSuccess(), (res: HttpErrorResponse) => this.onError());
    }

    onError(): void {
        alert('An error occured. Please try again later.');
    }

    onSaveResponseSuccess(): void {
        // alert('Save success!');
    }
}
