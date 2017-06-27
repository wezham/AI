
tests :-
  weird_sum_tests,
  same_name_tests,
  log_table_tests,
  paruns_tests,
  is_heap_tests.

weird_sum_tests :-
  test1,
  test2,
  test3,
  test4,
  test5,
  test6,
  test7.


test1 :-
  weird_sum([0], Result),
  Result =:= 0.

test2 :-
  weird_sum([3], Result),
  Result =:= 0.

test3 :-
  weird_sum([5], Result),
  Result =:= 25.

test4 :-
  weird_sum([-78], Result),
  Result =:= -78.

test5 :-
  weird_sum([-2], Result),
  weird_sum([2], Result1),
  Result =:= Result1.

test6 :-
  weird_sum([10,12,3,1,6,4,-7], Result),
  Result =:= 272.

test7 :-
  weird_sum([3,6,2,-1], Result),
  Result =:= 33.


parent(jim, brian).
parent(brian, jenny).
parent(pat, brian).
parent(lucy, jenny).
parent(amy, lucy).
parent(adam, lucy).
parent(julia, jim).
parent(chris, jim).
parent(julia, danielle).
parent(chris, danielle).
female(pat).
female(danielle).
female(julia).
female(lucy).
female(amy).
female(jenny).
male(jim).
male(adam).
male(chris).
male(brian).



same_name_tests :-
  test10,
  test11,
  test12,
  test13,
  test14,
  test15,
  test16,
  test17,
  test18,
  test19,
  test20.

test10 :-
  same_name(X, X).

test11 :-
  same_name(danielle, jim).

test12 :-
  same_name(brian, jenny).

test13 :-
  \+ same_name(lucy, jenny).

test14 :-
  same_name(chris, jenny).

test15 :-
  same_name(jenny, chris).

test16 :-
  \+ same_name(julia, brian).

test17 :-
  \+ same_name(adam, jenny).

test18 :-
  \+ same_name(jenny, amy).

test19 :-
  same_name(jenny, jim).

test20 :-
  \+ same_name(pat, brian).


log_table_tests :-
  test21,
  test22,
  test23.

test21 :-
  log_table([4.2,5.3,9.8,23.1],Result),
  Result == [[4.2, 1.4350845252893227],[5.3, 1.667706820558076],[9.8, 2.2823823856765264],[23.1, 3.139832617527748]].

test22 :-
  log_table([5,4,7,11,9.7],Result),
  Result == [[5, 1.6094379124341003],[4, 1.3862943611198906],[7, 1.9459101490553132],[11, 2.3978952727983707],[9.7, 2.272125885509337]].

test23 :-
  log_table([1,3.7,5], Result),
  Result = [[1, 0.0], [3.7, 1.308332819650179], [5, 1.6094379124341003]].


paruns_tests :-
  test31,
  test32,
  test33,
  test34,
  test35,
  test36.

test31 :-
  paruns([],[]).

test32 :-
  paruns([1],[[1]]).

test33 :-
  paruns([2],[[2]]).

test34 :-
  paruns([1,3,5,6,9,11,15], [[1,3,5],[6],[9,11,15]]).

test35 :-
  paruns([2,4,6,11,8,10,12], [[2,4,6],[11],[8,10,12]]).

test36 :-
  paruns([1,8,2,9,3,10,4,11],[[1],[8,2],[9,3],[10,4],[11]]).


is_heap_tests :-
  test41,
  test42,
  test43,
  test44,
  test45,
  test46.

test41 :-
  is_heap(empty).

test42 :-
  is_heap(tree(empty,5,empty)).

test43 :-
  is_heap(tree(tree(empty, 2, empty),1,tree(tree(empty, 4, empty),3,tree(empty, 5, empty)))).

test44 :-
  \+ is_heap(tree(tree(empty, 5, empty),3,tree(tree(empty, 2, empty),4,tree(empty, 7, empty)))).

test45 :-
  \+ is_heap(tree(tree(tree(empty,4,empty),3,tree(empty,5,empty)),6,tree(tree(empty,9,empty),7,empty))).

test46 :-
  is_heap(tree(empty,3,tree(tree(empty,8,empty),5,tree(empty,7,empty)))).





% -
