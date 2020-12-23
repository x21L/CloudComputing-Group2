namespace BookStoreSearch.Entity
{
    public class Book : IStoreable
    {
        public string Id { get; set; }
        
        public string Name { get; set; }

        public string Description { get; set; }

        public string Author { get; set; }
    }
}
