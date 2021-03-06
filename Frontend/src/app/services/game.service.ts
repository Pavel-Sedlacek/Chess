import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Game} from '../../data/game';
import {IFigure} from '../../data/iFigure';
import {Move} from '../../data/move';
import {ResponseMessage} from '../../data/responseMessage';
import {Teams} from '../../data/teams';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private readonly httpClient: HttpClient) {}

  getByUser(userId: number): Observable<Game[]> {
    return this.httpClient.get('/chess/game/user/' + userId) as Observable<Game[]>;
  }
  getBoard(id: number): Observable<IFigure[][]> {
    return this.httpClient.get('chess/game/' + id + '/sync') as Observable<IFigure[][]>;
  }
  makeMove(move: Move, id): Observable<ResponseMessage>{
    return this.httpClient.put('chess/game/' + id + '/play', move) as Observable<ResponseMessage>;
  }
  getTurn(id): Observable<Teams>{
    return this.httpClient.get('chess/game/' + id + '/turn') as Observable<Teams>;
  }

  getTeam(id): Observable<Teams>{
    return this.httpClient.get('chess/game/' + id + '/myTeam') as Observable<Teams>;
  }
  // github
}
