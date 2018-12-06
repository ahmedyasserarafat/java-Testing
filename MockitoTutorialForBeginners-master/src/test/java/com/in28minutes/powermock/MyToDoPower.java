package com.in28minutes.powermock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.in28minutes.business.TodoBusinessImpl;
import com.in28minutes.data.api.TodoService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SystemUnderTest.class,UtilityClass.class /*To be able to mock the Constructor, we need to add in the Class that creates the new object*/})

public class MyToDoPower {

	@Mock
	Dependency dependencyMock;

	@InjectMocks
	SystemUnderTest systemUnderTest;
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Mock
	TodoService todoService;
	
	@InjectMocks
	TodoBusinessImpl todoBusinessImpl;
	
	@Captor
	ArgumentCaptor<String>  captor;
	


	
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

	@Test
	public void powerMockito_CallingAPrivateMethod() throws Exception {
		when(dependencyMock.retrieveAllStats()).thenReturn(
				Arrays.asList(1, 2, 3));
		long value = (Long) Whitebox.invokeMethod(systemUnderTest,
				"privateMethodUnderTest");
		assertEquals(6, value);
		
		
		ArrayList<String> list=mock(ArrayList.class);
		
		stub(list.size()).toReturn(5);
		
		PowerMockito.whenNew(ArrayList.class).withNoArguments().thenReturn(list);
		
		assertEquals(5, systemUnderTest.methodUsingAnArrayListConstructor());
				
		
	}
	
	@Test
	public void powerMockito_MockingAStaticMethodCall() {

		when(dependencyMock.retrieveAllStats()).thenReturn(
				Arrays.asList(1, 2, 3));

		PowerMockito.mockStatic(UtilityClass.class);

		when(UtilityClass.staticMethod(anyLong())).thenReturn(150);

		assertEquals(150, systemUnderTest.methodCallingAStaticMethod());

		//To verify a specific method call
		//First : Call PowerMockito.verifyStatic() 
		//Second : Call the method to be verified
		PowerMockito.verifyStatic();
		UtilityClass.staticMethod(1 + 2 + 3);

		// verify exact number of calls
		//PowerMockito.verifyStatic(Mockito.times(1));

	}

}
