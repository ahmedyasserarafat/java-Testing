package com.in28minutes.powermock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.in28minutes.business.TodoBusinessImpl;
import com.in28minutes.data.api.TodoService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UtilityClass.class,SystemUnderTest.class})
public class MockingStaticMethodTest {
	
	//Specific Runner
	//Initialize UtilityClass.class
	//mock

	@Mock
	Dependency dependency;

	@InjectMocks
	SystemUnderTest systemUnderTest;
	
	@Mock
	ArrayList mockList;
	
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock
	TodoService todoServiceMock;
	
	@InjectMocks
	TodoBusinessImpl todoBusinessImpl;

	@Captor
	ArgumentCaptor<String> stringArgumentCaptor;

	@Test
	public void testRetrieveTodosRelatedToSpring_usingAMock() {

		List<Integer> stats = Arrays.asList(1, 2, 3);

		when(dependency.retrieveAllStats()).thenReturn(stats);
		when(dependency.res()).thenReturn(0L);
		
		PowerMockito.mockStatic(UtilityClass.class);
		
		when(UtilityClass.staticMethod(6)).thenReturn(150);
		
		int result = systemUnderTest.methodCallingAStaticMethod();
		
		assertEquals(150, result);
		
		PowerMockito.verifyStatic();
		UtilityClass.staticMethod(6);
		
		
	}
	
	@Test
	public void testBadNames() throws Exception {

		List<Integer> stats = Arrays.asList(1, 2, 3);
		
		when(mockList.size()).thenReturn(10);
		
		PowerMockito.whenNew(ArrayList.class).withAnyArguments().thenReturn(mockList);
		
		int size = systemUnderTest.methodUsingAnArrayListConstructor();
		
		assertEquals(10, size);
		
	}
	
	@Test
	public void testRetrieveTodosRelatedToSpring_usingAMock1() throws Exception {

		List<Integer> stats = Arrays.asList(1, 2, 3);

		when(dependency.retrieveAllStats()).thenReturn(stats);
				
		long result = Whitebox.invokeMethod(systemUnderTest, "privateMethodUnderTest");
		
		assertEquals(6, result);		
		
	}
	
	@Test
	public void testRetrieveTodosRelatedToSpring_usingAMock2() {
				
		List<String> todos = Arrays.asList("Learn Spring MVC", "Learn Spring",
				"Learn to Dance");
		
		when(todoServiceMock.retrieveTodos("Dummy")).thenReturn(todos);
				
		List<String> filteredTodos = todoBusinessImpl
				.retrieveTodosRelatedToSpring("Dummy");
		
		assertEquals(2, filteredTodos.size());
	}
}