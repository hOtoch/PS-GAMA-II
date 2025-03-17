import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITurma } from '../turma.model';

@Component({
  selector: 'jhi-turma-detail',
  templateUrl: './turma-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TurmaDetailComponent {
  turma = input<ITurma | null>(null);

  previousState(): void {
    window.history.back();
  }
}
