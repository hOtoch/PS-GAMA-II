import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ILocalizacao } from '../localizacao.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../localizacao.test-samples';

import { LocalizacaoService } from './localizacao.service';

const requireRestSample: ILocalizacao = {
  ...sampleWithRequiredData,
};

describe('Localizacao Service', () => {
  let service: LocalizacaoService;
  let httpMock: HttpTestingController;
  let expectedResult: ILocalizacao | ILocalizacao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LocalizacaoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Localizacao', () => {
      const localizacao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(localizacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Localizacao', () => {
      const localizacao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(localizacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Localizacao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Localizacao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Localizacao', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLocalizacaoToCollectionIfMissing', () => {
      it('should add a Localizacao to an empty array', () => {
        const localizacao: ILocalizacao = sampleWithRequiredData;
        expectedResult = service.addLocalizacaoToCollectionIfMissing([], localizacao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(localizacao);
      });

      it('should not add a Localizacao to an array that contains it', () => {
        const localizacao: ILocalizacao = sampleWithRequiredData;
        const localizacaoCollection: ILocalizacao[] = [
          {
            ...localizacao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLocalizacaoToCollectionIfMissing(localizacaoCollection, localizacao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Localizacao to an array that doesn't contain it", () => {
        const localizacao: ILocalizacao = sampleWithRequiredData;
        const localizacaoCollection: ILocalizacao[] = [sampleWithPartialData];
        expectedResult = service.addLocalizacaoToCollectionIfMissing(localizacaoCollection, localizacao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(localizacao);
      });

      it('should add only unique Localizacao to an array', () => {
        const localizacaoArray: ILocalizacao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const localizacaoCollection: ILocalizacao[] = [sampleWithRequiredData];
        expectedResult = service.addLocalizacaoToCollectionIfMissing(localizacaoCollection, ...localizacaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const localizacao: ILocalizacao = sampleWithRequiredData;
        const localizacao2: ILocalizacao = sampleWithPartialData;
        expectedResult = service.addLocalizacaoToCollectionIfMissing([], localizacao, localizacao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(localizacao);
        expect(expectedResult).toContain(localizacao2);
      });

      it('should accept null and undefined values', () => {
        const localizacao: ILocalizacao = sampleWithRequiredData;
        expectedResult = service.addLocalizacaoToCollectionIfMissing([], null, localizacao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(localizacao);
      });

      it('should return initial array if no Localizacao is added', () => {
        const localizacaoCollection: ILocalizacao[] = [sampleWithRequiredData];
        expectedResult = service.addLocalizacaoToCollectionIfMissing(localizacaoCollection, undefined, null);
        expect(expectedResult).toEqual(localizacaoCollection);
      });
    });

    describe('compareLocalizacao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLocalizacao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 19197 };
        const entity2 = null;

        const compareResult1 = service.compareLocalizacao(entity1, entity2);
        const compareResult2 = service.compareLocalizacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 19197 };
        const entity2 = { id: 21366 };

        const compareResult1 = service.compareLocalizacao(entity1, entity2);
        const compareResult2 = service.compareLocalizacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 19197 };
        const entity2 = { id: 19197 };

        const compareResult1 = service.compareLocalizacao(entity1, entity2);
        const compareResult2 = service.compareLocalizacao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
