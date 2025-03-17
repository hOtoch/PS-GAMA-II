import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ILocalizacao } from 'app/entities/localizacao/localizacao.model';
import { LocalizacaoService } from 'app/entities/localizacao/service/localizacao.service';
import { ITurma } from 'app/entities/turma/turma.model';
import { TurmaService } from 'app/entities/turma/service/turma.service';
import { IAluno } from '../aluno.model';
import { AlunoService } from '../service/aluno.service';
import { AlunoFormService } from './aluno-form.service';

import { AlunoUpdateComponent } from './aluno-update.component';

describe('Aluno Management Update Component', () => {
  let comp: AlunoUpdateComponent;
  let fixture: ComponentFixture<AlunoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alunoFormService: AlunoFormService;
  let alunoService: AlunoService;
  let localizacaoService: LocalizacaoService;
  let turmaService: TurmaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AlunoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AlunoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlunoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alunoFormService = TestBed.inject(AlunoFormService);
    alunoService = TestBed.inject(AlunoService);
    localizacaoService = TestBed.inject(LocalizacaoService);
    turmaService = TestBed.inject(TurmaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call localizacao query and add missing value', () => {
      const aluno: IAluno = { id: 9303 };
      const localizacao: ILocalizacao = { id: 19197 };
      aluno.localizacao = localizacao;

      const localizacaoCollection: ILocalizacao[] = [{ id: 19197 }];
      jest.spyOn(localizacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: localizacaoCollection })));
      const expectedCollection: ILocalizacao[] = [localizacao, ...localizacaoCollection];
      jest.spyOn(localizacaoService, 'addLocalizacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aluno });
      comp.ngOnInit();

      expect(localizacaoService.query).toHaveBeenCalled();
      expect(localizacaoService.addLocalizacaoToCollectionIfMissing).toHaveBeenCalledWith(localizacaoCollection, localizacao);
      expect(comp.localizacaosCollection).toEqual(expectedCollection);
    });

    it('Should call Turma query and add missing value', () => {
      const aluno: IAluno = { id: 9303 };
      const turma: ITurma = { id: 31584 };
      aluno.turma = turma;

      const turmaCollection: ITurma[] = [{ id: 31584 }];
      jest.spyOn(turmaService, 'query').mockReturnValue(of(new HttpResponse({ body: turmaCollection })));
      const additionalTurmas = [turma];
      const expectedCollection: ITurma[] = [...additionalTurmas, ...turmaCollection];
      jest.spyOn(turmaService, 'addTurmaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aluno });
      comp.ngOnInit();

      expect(turmaService.query).toHaveBeenCalled();
      expect(turmaService.addTurmaToCollectionIfMissing).toHaveBeenCalledWith(
        turmaCollection,
        ...additionalTurmas.map(expect.objectContaining),
      );
      expect(comp.turmasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const aluno: IAluno = { id: 9303 };
      const localizacao: ILocalizacao = { id: 19197 };
      aluno.localizacao = localizacao;
      const turma: ITurma = { id: 31584 };
      aluno.turma = turma;

      activatedRoute.data = of({ aluno });
      comp.ngOnInit();

      expect(comp.localizacaosCollection).toContainEqual(localizacao);
      expect(comp.turmasSharedCollection).toContainEqual(turma);
      expect(comp.aluno).toEqual(aluno);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAluno>>();
      const aluno = { id: 15328 };
      jest.spyOn(alunoFormService, 'getAluno').mockReturnValue(aluno);
      jest.spyOn(alunoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aluno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aluno }));
      saveSubject.complete();

      // THEN
      expect(alunoFormService.getAluno).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alunoService.update).toHaveBeenCalledWith(expect.objectContaining(aluno));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAluno>>();
      const aluno = { id: 15328 };
      jest.spyOn(alunoFormService, 'getAluno').mockReturnValue({ id: null });
      jest.spyOn(alunoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aluno: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aluno }));
      saveSubject.complete();

      // THEN
      expect(alunoFormService.getAluno).toHaveBeenCalled();
      expect(alunoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAluno>>();
      const aluno = { id: 15328 };
      jest.spyOn(alunoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aluno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alunoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLocalizacao', () => {
      it('Should forward to localizacaoService', () => {
        const entity = { id: 19197 };
        const entity2 = { id: 21366 };
        jest.spyOn(localizacaoService, 'compareLocalizacao');
        comp.compareLocalizacao(entity, entity2);
        expect(localizacaoService.compareLocalizacao).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTurma', () => {
      it('Should forward to turmaService', () => {
        const entity = { id: 31584 };
        const entity2 = { id: 27123 };
        jest.spyOn(turmaService, 'compareTurma');
        comp.compareTurma(entity, entity2);
        expect(turmaService.compareTurma).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
