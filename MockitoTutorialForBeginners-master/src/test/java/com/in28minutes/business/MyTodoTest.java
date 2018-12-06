package com.in28minutes.business;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.in28minutes.data.api.TodoService;
import com.in28minutes.powermock.SystemUnderTest;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SystemUnderTest.class /*To be able to mock the Constructor, we need to add in the Class that creates the new object*/})

public class MyTodoTest {
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	TodoService todoService;
	
	@InjectMocks
	TodoBusinessImpl todoBusinessImpl;
	
	@Captor
	ArgumentCaptor<String>  captor;
	
	

	@InjectMocks
	SystemUnderTest systemUnderTest;
	
	

	
	@Test
	public void usingMockito() {
		List<String> allTodos = Arrays.asList("Learn Spring MVC",
				"Learn Spring", "Learn to Dance");

		doReturn(allTodos).when(todoService).retrieveTodos(Mockito.anyString());
		
		//given(todoService.retrieveTodos(Mockito.anyString())).willReturn(allTodos);
		
		List<String> filteredTodos =todoBusinessImpl.retrieveTodosRelatedToSpring("Ranga");
		
		assertThat(filteredTodos, hasSize(2));
		
		todoBusinessImpl.deleteTodosNotRelatedToSpring("Ranga");;
		
		
		verify(todoService,Mockito.times(1)).deleteTodo(captor.capture());
		
		verify(todoService,Mockito.never()).deleteTodo("Learn Spring MVC");
		
		verify(todoService).deleteTodo(captor.capture());
		
		assertThat("Learn to Dance", is(captor.getValue()));
		
		
		
	}


}
