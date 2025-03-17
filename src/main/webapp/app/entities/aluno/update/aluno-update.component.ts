import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ILocalizacao } from 'app/entities/localizacao/localizacao.model';
import { LocalizacaoService } from 'app/entities/localizacao/service/localizacao.service';
import { ITurma } from 'app/entities/turma/turma.model';
import { TurmaService } from 'app/entities/turma/service/turma.service';
import { AlunoService } from '../service/aluno.service';
import { IAluno } from '../aluno.model';
import { AlunoFormGroup, AlunoFormService } from './aluno-form.service';

@Component({
  selector: 'jhi-aluno-update',
  templateUrl: './aluno-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AlunoUpdateComponent implements OnInit {
  isSaving = false;
  aluno: IAluno | null = null;

  localizacaosCollection: ILocalizacao[] = [];
  turmasSharedCollection: ITurma[] = [];

  protected alunoService = inject(AlunoService);
  protected alunoFormService = inject(AlunoFormService);
  protected localizacaoService = inject(LocalizacaoService);
  protected turmaService = inject(TurmaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AlunoFormGroup = this.alunoFormService.createAlunoFormGroup();

  compareLocalizacao = (o1: ILocalizacao | null, o2: ILocalizacao | null): boolean => this.localizacaoService.compareLocalizacao(o1, o2);

  compareTurma = (o1: ITurma | null, o2: ITurma | null): boolean => this.turmaService.compareTurma(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aluno }) => {
      this.aluno = aluno;
      if (aluno) {
        this.updateForm(aluno);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aluno = this.alunoFormService.getAluno(this.editForm);
    if (aluno.id !== null) {
      this.subscribeToSaveResponse(this.alunoService.update(aluno));
    } else {
      this.subscribeToSaveResponse(this.alunoService.create(aluno));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAluno>>): void {
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

  protected updateForm(aluno: IAluno): void {
    this.aluno = aluno;
    this.alunoFormService.resetForm(this.editForm, aluno);

    this.localizacaosCollection = this.localizacaoService.addLocalizacaoToCollectionIfMissing<ILocalizacao>(
      this.localizacaosCollection,
      aluno.localizacao,
    );
    this.turmasSharedCollection = this.turmaService.addTurmaToCollectionIfMissing<ITurma>(this.turmasSharedCollection, aluno.turma);
  }

  protected loadRelationshipsOptions(): void {
    this.localizacaoService
      .query({ filter: 'aluno-is-null' })
      .pipe(map((res: HttpResponse<ILocalizacao[]>) => res.body ?? []))
      .pipe(
        map((localizacaos: ILocalizacao[]) =>
          this.localizacaoService.addLocalizacaoToCollectionIfMissing<ILocalizacao>(localizacaos, this.aluno?.localizacao),
        ),
      )
      .subscribe((localizacaos: ILocalizacao[]) => (this.localizacaosCollection = localizacaos));

    this.turmaService
      .query()
      .pipe(map((res: HttpResponse<ITurma[]>) => res.body ?? []))
      .pipe(map((turmas: ITurma[]) => this.turmaService.addTurmaToCollectionIfMissing<ITurma>(turmas, this.aluno?.turma)))
      .subscribe((turmas: ITurma[]) => (this.turmasSharedCollection = turmas));
  }
}
