namespace BookStoreSearch.Entity
{
    public class SearchSettings
    {
        /// <summary>
        /// From Index if pagination is being used.
        /// </summary>
        public int From { get; set; }

        /// <summary>
        /// Amount of returned results if pagination is being used.
        /// </summary>
        public int Size { get; set; }
    }
}
