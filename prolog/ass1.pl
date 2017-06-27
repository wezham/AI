:- [tests].
% << ( Q1 ) >>
% Base case empty list, return 0
weird_sum([], 0).

% Cond1 - Num greater than 5
weird_sum([Head|Tail], Result) :-
  weird_sum(Tail, Rest),
  Head >= 5,
  Result is Rest + (Head*Head).
% Cond2 -  Num less than 2
weird_sum([Head|Tail], Result) :-
  weird_sum(Tail, Rest),
  Head =< 2,
  Result is Rest - abs(Head).
% Cond3 -  Num is sumwhere in between ( funny )
weird_sum([Head|Tail], Result) :-
  weird_sum(Tail, Rest),
  Head > 2, Head < 5,
  Result is Rest + 0.

% << ( Q2 ) >>
% Person 1 has the name has p2 if p2 is the descendant of p1 and p1 is male ]
% Same logic for P2, P1 just the reverse
% Also if they are sibilings, i.e. parent is the same and male

same_name(X, X).

same_name(P1, P2) :-
  male_descendant(P1, P2),
  male(P1).
same_name(P1, P2) :-
  male_descendant(P2, P1),
  male(P2).
same_name(P1, P2) :-
  parent(Parent, P1),
  parent(Parent, P2),
  male(Parent).

male_descendant(Person, Descendant) :-
  parent(Person, Descendant),
  male(Person).
% Male descendant means the chain of descendance cannot include a female
male_descendant(Person, Descendant) :-
  parent(Person, Child),
  male(Child),
  male_descendant(Child, Descendant).
% End


% << ( Q3 ) >>
% This one is simple, return and empty list and keep putting lists in the list of lists
log_table([], [] ).
log_table([Head|Tail], List ) :-
  log_table(Tail, Rest),
  Log is log(Head),
  List = [ [Head, Log] | Rest ].


% << ( Q4 ) >>
% Basis of this is 4 situations listed below
% Numtoinsert: 4, [ [2]]
% Numtoinsert: 2, [ [3,5]]
% Numtoinsert: 5, [ [3,5]]
% Numtoinsert: 7, [ [4,6]]
paruns([Head], [[Head]]).
paruns([], []).
%Even and head is even
paruns([Head | Tail], RunList) :-
  paruns(Tail, Rest),
  [ H | T ] = Rest,
  0 is Head mod 2,
  [ H2 | _ ] = H,
  0 is H2 mod 2,
  NewHead = [ Head | H ],
  RunList = [ NewHead | T ].
%Even and head is odd
paruns([Head | Tail], RunList) :-
  paruns(Tail, Rest),
  [ H | _ ] = Rest,
  0 is Head mod 2,
  [ H2 | _ ] = H,
  not(0 is H2 mod 2),
  RunList = [ [Head] | Rest].
%Odd and head is even
paruns([Head | Tail], RunList) :-
  paruns(Tail, Rest),
  [ H | _ ] = Rest,
  not(0 is Head mod 2),
  [ H2 | _ ] = H,
  0 is H2 mod 2,
  RunList = [ [Head] | Rest].
%Odd and head is odd
paruns([Head | Tail], RunList) :-
  paruns(Tail, Rest),
  [ H | T ] = Rest,
  not(0 is Head mod 2),
  [ H2 | _ ] = H,
  not(0 is H2 mod 2),
  NewHead = [ Head | H ],
  RunList = [ NewHead | T ].


% << ( Q5 ) >>

% Helper Function for is_heap
is_empty(empty).
is_heap(empty).
is_heap(tree(empty, _, empty)).
%Left tree empty
%Right is not
is_heap(tree(Left, Data, Right)) :-
  is_empty(Left),
  not(is_empty(Right)),
  tree(_, RData, _) = Right,
  Data =< RData,
  is_heap(Right).
%Right tree empty - Left is not
is_heap(tree(Left, Data, Right)) :-
  is_empty(Right),
  not(is_empty(Left)),
  tree(_, LData, _) = Left,
  Data =< LData,
  is_heap(Left).
%Both not empty
is_heap(tree(Left, Data, Right)) :-
  tree(_, LData, _) = Left,
  Data =< LData,
  tree(_, RData, _) = Right,
  Data =< RData,
  is_heap(Left),
  is_heap(Right).
