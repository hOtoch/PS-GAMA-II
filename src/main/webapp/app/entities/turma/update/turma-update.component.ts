import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfessor } from 'app/entities/professor/professor.model';
import { ProfessorService } from 'app/entities/professor/service/professor.service';
import { ITurma } from '../turma.model';
import { TurmaService } from '../service/turma.service';
import { TurmaFormGroup, TurmaFormService } from './turma-form.service';

@Component({
  selector: 'jhi-turma-update',
  templateUrl: './turma-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TurmaUpdateComponent implements OnInit {
  isSaving = false;
  turma: ITurma | null = null;

  professorsSharedCollection: IProfessor[] = [];

  protected turmaService = inject(TurmaService);
  protected turmaFormService = inject(TurmaFormService);
  protected professorService = inject(ProfessorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TurmaFormGroup = this.turmaFormService.createTurmaFormGroup();

  compareProfessor = (o1: IProfessor | null, o2: IProfessor | null): boolean => this.professorService.compareProfessor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ turma }) => {
      this.turma = turma;
      if (turma) {
        this.updateForm(turma);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const turma = this.turmaFormService.getTurma(this.editForm);
    if (turma.id !== null) {
      this.subscribeToSaveResponse(this.turmaService.update(turma));
    } else {
      this.subscribeToSaveResponse(this.turmaService.create(turma));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITurma>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(turma: ITurma): void {
    this.turma = turma;
    this.turmaFormService.resetForm(this.editForm, turma);

    this.professorsSharedCollection = this.professorService.addProfessorToCollectionIfMissing<IProfessor>(
      this.professorsSharedCollection,
      ...(turma.professors ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.professorService
      .query()
      .pipe(map((res: HttpResponse<IProfessor[]>) => res.body ?? []))
      .pipe(
        map((professors: IProfessor[]) =>
          this.professorService.addProfessorToCollectionIfMissing<IProfessor>(professors, ...(this.turma?.professors ?? [])),
        ),
      )
      .subscribe((professors: IProfessor[]) => (this.professorsSharedCollection = professors));
  }
}
