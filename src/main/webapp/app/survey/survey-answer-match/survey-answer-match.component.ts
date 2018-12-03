import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Challenger } from 'app/shared/model/challenger.model';

@Component({
    selector: 'jhi-survey-answer-match',
    templateUrl: './survey-answer-match.component.html',
    styles: []
})
export class SurveyAnswerMatchComponent implements OnInit {
    @Input()
    challengerOne: Challenger;
    @Input()
    challengerTwo: Challenger;
    @Output()
    winnerSelected = new EventEmitter<Challenger>();

    constructor() {}

    ngOnInit() {
        // Show loader while retrieving images from Flickr
        this.challengerOne = new Challenger('UNDEFINED_1', '/content/images/loader.gif');
        this.challengerTwo = new Challenger('UNDEFINED_2', '/content/images/loader.gif');
    }

    challengerClicked(challenger) {
        //TODO add start and end date to challenges
        this.winnerSelected.emit(challenger);
    }
}
