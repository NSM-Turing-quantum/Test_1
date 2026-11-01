#include <stdio.h>
#include <stdlib.h>

struct Node
{
    int data;
    struct Node* next;
};

struct Node* createNode(int data)
{
    struct Node* newNode = (struct Node*)malloc(sizeof(struct Node));

    newNode->data = data;
    newNode->next = NULL;

    return newNode;
}

void print(struct Node* head)
{
    struct Node* temp = head;

    while (temp->next != NULL)
    {
        printf("%d -> ", temp->data);
        temp = temp->next;
    }
    printf("%d \n", temp->data);
}

void freeList(struct Node* head)
{
    struct Node* temp;

    while (head != NULL)
    {
        temp = head;
        head = head->next;
        free(temp);
    }
}

int findMiddle(struct Node* head)
{
    if (head == NULL) return NULL;

    struct Node* slow = head;
    struct Node* fast = head;

    while (fast != NULL && fast->next != NULL)
    {
        slow = slow->next;
        fast = fast->next->next;
    }

    return slow->data;
}

int main()
{
    struct Node* head = createNode(1);
    head->next = createNode(2);
    head->next->next = createNode(3);
    head->next->next->next = createNode(4);
    head->next->next->next->next = createNode(5);

    printf("My list:   ");
    print(head);

    printf("Middle:    %d\n\n", findMiddle(head));

    free(head->next->next->next->next);
    head->next->next->next->next = NULL;

    printf("My list:   ");
    print(head);

    printf("Middle:    %d\n\n", findMiddle(head));

    freeList(head);

    return 0;
}
