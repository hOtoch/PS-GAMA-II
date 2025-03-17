import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IProfessor } from '../professor.model';

@Component({
  selector: 'jhi-professor-detail',
  templateUrl: './professor-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ProfessorDetailComponent {
  professor = input<IProfessor | null>(null);

  previousState(): void {
    window.history.back();
  }
}
